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
        $postID = $_POST['postID'];
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
        $sql = "INSERT INTO WATER(UserID, GroupID, PostID, Date) VALUES(?, ?, ?, CURDATE());";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("sii",$userID,$groupID,$postID);
        $result = $stmt->execute();

        if($result === true){
          //성공 시 동작
          $sql = "SELECT TodaysToDo, UserID FROM POST WHERE PostID = ?;";
          $stmt = $conn->prepare($sql);
          $stmt->bind_param("i",$postID);
          $stmt->execute();
          $result2 = $stmt->get_result();
          $row = mysqli_fetch_assoc($result2);

          $todoData = json_decode($row['TodaysToDo']);
          $targetUserID = $row['UserID'];
          $totalPriotity = 0;
          $checkedPriotity = 0;
          foreach($todoData as $item) {
            $totalPriotity += $item->priority;
            if($item->isChecked) {
                $checkedPriotity += $item->priority;
            }
          }
          $addGrowth = ceil($checkedPriotity/$totalPriotity * 10);
          //식물 성장
          $sql = "UPDATE PLANT SET Growth = Growth + ?, LastWatered = CURDATE() WHERE GroupID = ? AND UserID = ?;";
          $stmt = $conn->prepare($sql);
          $stmt->bind_param("iis",$addGrowth,$groupID,$targetUserID);
          $result = $stmt->execute();
          //그룹 성장
          $sql = "UPDATE GROUP_ SET Score = Score + ? WHERE GroupID = ?;";
          $stmt = $conn->prepare($sql);
          $stmt->bind_param("ii",$addGrowth,$groupID);
          $result = $stmt->execute();
          
          // 알림 전송 ========================
          $noticeRecvID = $targetUserID;
          $noticeSendID = $userID;
          $noticeMSG = "님이 물을 주었습니다.";
          $noticeType = 1; 
          $noticeTarget = $groupID;
          include('sendNotice.php');
          // =================================

          $outputFrame -> data = 1;
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
