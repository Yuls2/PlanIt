<?php header("Content-Type:text/html;charset=utf-8"); ?>
<?php
    require __DIR__ . '/../vendor/autoload.php';
    use Firebase\JWT\JWT;
    use Firebase\JWT\Key;

    // 토큰 header 양식 [authorization] => Bearer eyJ0eXAiOi...
    $headers = apache_request_headers();
    // 테스트 할 땐 아래 한 줄 주석을 풀고 하면 됩니다!
    //$headers['authorization'] = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vcGxhbml0MjAyMi5jYWZlMjQuY29tIiwiYXVkIjoicGxhbml0QXBwIiwidXNlcklEIjoicGxhbml0VGVzdDEiLCJ1c2VyTmFtZSI6Ilx1ZDUwY1x1Yjc5OFx1YjJkYlx1YjllODEifQ.1uWb9NRO2BAdEPn-V9CqGvpCvLYN6G7BnsD6HO-xH30";
    $authHeader =isset($headers['authorization'])?$headers['authorization']:null;

    //최종 출력할 결과
    $outputFrame = new stdClass;
    $outputFrame -> result = 0;             //오류 코드: 0 -> 정상, 나머지 오류는 임의배정
    $outputFrame -> message = "success";    //오류 메세지: 각 오류 상황에서 재설정

    //토큰이 존재할 경우
    if(isset($authHeader)) {
      $splitAuthHeader = explode(" ", $authHeader);
      $jwt = $splitAuthHeader[1];
      try{
        $secretkey = '------';
        $decoded = JWT::decode($jwt, new Key($secretkey, 'HS256'));
        $decodedJwt = (array) $decoded;
        //valid 한 경우
        $userID = $decodedJwt['userID'];

        /* post 파라미터 받는 부분 */
        //$id = $_POST['userID'];
        /* ======================== */

        /* DB연결하는 부분 */
        $host = 'localhost';
        $user = 'planit2022';
        $password = '------';
        $dbName = 'planit2022';

        header('Content-Type: application/json');

        $conn = new mysqli($host, $user, $password, $dbName);
        if(!($conn)){
            echo "db 연결 실패: " . mysqli_connect_error();
        }
        /* ======================== */

        $sql = "-- 한 유저의 모든 그룹
                SELECT * FROM
                (SELECT ug.GroupID, g.GroupName, ug.Goal FROM (SELECT GroupID, Goal FROM USER_GROUP WHERE UserID = ?) ug
                JOIN GROUP_ g ON ug.GroupID = g.GroupID) AS a
                JOIN
                -- 한 유저의 모든 체크아이템
                (SELECT  GroupID, CheckID, DayOfWeek, TargetDate, CheckName, IsChecked, Priority, Type FROM CHECKITEM WHERE UserID = ?) AS b
                ON a.GroupID = b.GroupID
                -- 그룹 아이디 오름차순 정렬, 우선순위 내림차순 정렬
                ORDER BY a.GroupID, Priority DESC, b.CheckID;";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ss",$userID,$userID);
        $stmt->execute();
        $result = $stmt->get_result();
        $output = array(); // 데이터 넣을 배열 생성

        //json 배열 형식으로 결과 출력
        if(mysqli_num_rows($result) > 0){ // 쿼리 결과로 1행 이상 존재한다면
            $checkItem = array(); // 체크 아이템 넣을 배열 생성

            while($row = mysqli_fetch_assoc($result)) {
                if($group['GroupID'] != $row['GroupID']){ // 처음 또는 다음 그룹으로 넘어갔을 때
                    if(count($checkItem) > 0){ // 체크 아이템이 있으면
                        array_push($output, 
                            array(
                                'userID' => $userID,
                                'groupID' => $group['GroupID'],
                                'groupName' => $group['GroupName'],
                                'userGoal' => $group['Goal'],
                                'checkItem' => $checkItem
                            )
                        ); // 체크아이템 저장된 정보 출력 배열에 추가
                        $checkItem = array(); // 체크 아이템 넣을 배열 초기화, // 궁금한것? 메모리 deallocate 안해도됨?? 메모리 누수 걱정쓰
                    }
                }

                // 현재 그룹 정보 저장
                $group = array('GroupID'=>$row['GroupID'], 'GroupName'=>$row['GroupName'], 'Goal'=>$row['Goal']);

                // 체크리스트 저장
                if($row['Type'] == 0){ // type 0: 요일별
                    array_push($checkItem,
                        array(
                            'checkID' => $row['CheckID'],  // => 왼쪽 문자열: json에서 속성 이름, 오른쪽 값: json에서 속성 값
                            'type' => $row['Type'],
                            'day' => $row['DayOfWeek'],
                            'title' => $row['CheckName'],
                            'isChecked' => $row['IsChecked']==1?true:false,
                            'priority' => $row['Priority'],
                        )
                    );
                }
                else if($row['Type'] == 1){ // type 1: 날짜별
                    array_push($checkItem,
                        array(
                            'checkID' => $row['CheckID'],  // => 왼쪽 문자열: json에서 속성 이름, 오른쪽 값: json에서 속성 값
                            'type' => $row['Type'],
                            'date' => $row['TargetDate'],
                            'title' => $row['CheckName'],
                            'isChecked' => $row['IsChecked']==1?true:false,
                            'priority' => $row['Priority'],
                        )
                    );
                }
                else{
                    // 오류
                }
            }
            if(count($checkItem) > 0){ // 체크 아이템이 있으면
                array_push($output, 
                    array(
                        'userID' => $userID,
                        'groupID' => $group['GroupID'],
                        'groupName' => $group['GroupName'],
                        'userGoal' => $group['Goal'],
                        'checkItem' => $checkItem
                    )
                ); // 체크아이템 저장된 정보 출력 배열에 추가
            }
        } else{
            $outputFrame -> result = 3;
            $outputFrame -> message = "항목이 존재하지 않습니다.";
        }

        $stmt->close();
        mysqli_close($conn);

        //출력할 결과에 data 추가
        $outputFrame -> data = $output;


      } catch (\Exception $e) { // invalid한 경우
          $outputFrame -> result = 2;
          $outputFrame -> message = "유효하지 않은 토큰입니다.";
      }
    }
    else { //토큰이 없는 경우
      $outputFrame -> result = 1;
      $outputFrame -> message = "토큰이 누락되었습니다.";
    }

    echo json_encode($outputFrame, JSON_UNESCAPED_UNICODE);
?>
