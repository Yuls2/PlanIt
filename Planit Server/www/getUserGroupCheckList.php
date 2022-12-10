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
        $groupID = $_POST['groupID'];
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

        // $sql = "SELECT *
        //         FROM CHECKITEM AS ch LEFT JOIN USER_GROUP AS u ON ch.UserID = u.UserID AND ch.GroupID = u.GroupID WHERE ch.GroupID = ? AND u.UserID = ? ORDER BY ch.Priority DESC;";
        $output = new stdClass;
        $output -> userID = $userID;
        $output -> groupID = $groupID;

        $sql = "SELECT Goal
                FROM USER_GROUP
                WHERE GroupID = ? AND UserID = ?;";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("is", $groupID, $userID);
        $stmt->execute();
        $result = $stmt->get_result();
        $row = mysqli_fetch_assoc($result);
        $output -> userGoal = $row['Goal'];
        
        $sql = "SELECT *
                FROM CHECKITEM
                WHERE GroupID = ? AND UserID = ? 
                ORDER BY Priority DESC;";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("is", $groupID, $userID);
        $stmt->execute();
        $result = $stmt->get_result();


        $checkListOutput = array();

        //json 배열 형식으로 결과 출력
        if(mysqli_num_rows($result) > 0){ // 쿼리 결과로 1행 이상 존재한다면
            while($row = mysqli_fetch_assoc($result)) {
                array_push($checkListOutput,
                    array(
                        'checkID' => $row['CheckID'],
                        'type' => $row['Type'],
                        'day' => $row['DayOfWeek'],
                        'date' => $row['TargetDate'],
                        'title' => $row['CheckName'],
                        'isChecked' => $row['IsChecked']==1?true:false,
                        'priority' => $row['Priority']
                    )
                );
            }
            $output -> checkItem = $checkListOutput;
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

    echo json_encode($outputFrame);
?>
