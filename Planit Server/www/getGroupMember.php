<?php header("Content-Type:text/html;charset=utf-8"); ?>
<?php
    require __DIR__ . '/../vendor/autoload.php';
    use Firebase\JWT\JWT;
    use Firebase\JWT\Key;

    // 토큰 header 양식 [authorization] => Bearer eyJ0eXAiOi...
    $headers = apache_request_headers();
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
        $targetUserID = $_POST['targetUserID'];
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

        $sql = "SELECT *,
                    (SELECT NickName
                    FROM USER AS u
                    WHERE u.UserID = m.UserID) AS NickName
                FROM USER_GROUP AS m
                WHERE m.UserID = ? AND m.GroupID = ?;";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("si",$targetUserID,$groupID);
        $stmt->execute();
        $result = $stmt->get_result();
        $output = new stdClass;

        //json 형식으로 결과 출력
        if(mysqli_num_rows($result) > 0){ // 쿼리 결과로 1행 이상 존재한다면
            if($row = mysqli_fetch_assoc($result)) {
                //유저의 포스트 뽑아오기
                $sql = "SELECT *, p.Date AS pDate,
                            (SELECT COUNT(*)
                            FROM WATER AS w
                            WHERE w.PostID = p.PostID) AS WaterNum,
                            (SELECT NickName
                            FROM USER AS u
                            WHERE u.UserID = p.UserID) AS NickName
                        FROM POST AS p LEFT JOIN USER_GROUP AS m
                                      ON p.GroupID = m.GroupID AND p.UserID = m.UserID
                        WHERE p.UserID = ? AND p.GroupID = ?
                        ORDER BY PostID DESC;";
                $stmt = $conn->prepare($sql);
                $stmt->bind_param("si",$targetUserID,$groupID);
                $stmt->execute();
                $result = $stmt->get_result();
                $subOutput = array();

                //json 배열 형식으로 결과 출력
                if(mysqli_num_rows($result) > 0){ // 쿼리 결과로 1행 이상 존재한다면
                    while($row2 = mysqli_fetch_assoc($result)) {
                        array_push($subOutput,
                            array(
                                'postID' => $row2['PostID'],  // => 왼쪽 문자열: json에서 속성 이름, 오른쪽 값: json에서 속성 값
                                'userID' => $row2['UserID'],
                                'userName' => $row2['NickName'],
                                'groupID' => $row2['GroupID'],
                                'userGoal' => $row2['Goal'],
                                'imageURL' => $row2['ImageURL'],
                                'comment' => $row2['Comment'],
                                'date' => $row2['pDate'],
                                'waterNum' => $row2['WaterNum'],
                                'checkItem' => json_decode($row2['TodaysToDo'])
                            )
                        );
                    }
                }

                $output -> userID = $row['UserID'];
                $output -> userName = $row['NickName'];
                $output -> groupID = $row['GroupID'];
                $output -> userGoal = $row['Goal'];
                $output -> status = $row['StatusMessage'];
                $output -> date = $row['Date'];
                $output -> isManager = $row['IsManager']==1?true:false;
                $output -> postList = $subOutput;
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

    echo json_encode($outputFrame);
?>