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
        $type = $_POST['type'];
        $day = $_POST['day'];
        $date = $_POST['date'];
        $title = $_POST['title'];
        $priority = $_POST['priority'];
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

        $stmt = mysqli_init();

        if($type == 0 && isset($day)){    // type 0: 요일 day
          $sql = "INSERT INTO CHECKITEM(UserID, GroupID, DayOfWeek, CheckName, Priority, Type) VALUES(?, ?, ?, ?, ?, 0);";
          $stmt = $conn->prepare($sql);
          $stmt->bind_param("siisi",$userID,$groupID,$day,$title,$priority);
          $result = $stmt->execute();
        }  
        else if($type == 1 && isset($date)){   // type 1 : 날짜 date
          $sql = "INSERT INTO CHECKITEM(UserID, GroupID, TargetDate, CheckName, Priority, Type) VALUES(?, ?, ?, ?, ?, 1);";
          $stmt = $conn->prepare($sql);
          $stmt->bind_param("sissi",$userID,$groupID,$date,$title,$priority);
          $result = $stmt->execute();
        }
        else {   // type error 이거나 day/date 모두 NULL
          //
        }

        if($result === true){
          //성공 시 동작
          //이 예에서는 마지막으로 삽입된 아이디 출력
          $sql = "SELECT LAST_INSERT_ID() AS checkID;";
          $result2 = mysqli_query($conn, $sql);
          $row = mysqli_fetch_assoc($result2);

          $outputFrame -> data = $row['checkID'];
        }
        else {
          //실패시 동작
          $outputFrame -> result = 3;
          $outputFrame -> message = $stmt->error;
        }
        /* */

        $stmt->close();
        mysqli_close($conn);

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
