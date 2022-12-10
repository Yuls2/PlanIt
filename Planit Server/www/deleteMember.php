<?php header("Content-Type:text/html;charset=utf-8"); ?>
<?php
    require __DIR__ . '/../vendor/autoload.php';
    use Firebase\JWT\JWT;
    use Firebase\JWT\Key;

    // 토큰 header 양식 [authorization] => Bearer eyJ0eXAiOi...
    $headers = apache_request_headers();
    $authHeader = isset($headers['authorization'])?$headers['authorization']:null;

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

        $stmt = mysqli_init();
        $sql = "SELECT IsManager FROM USER_GROUP WHERE UserID = ? AND GroupID = ?;";   // 그룹의 관리자인지 정보 가져옴
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("si",$userID,$groupID);
        $stmt->execute();
        $result = $stmt->get_result();
  
        if(mysqli_num_rows($result) > 0){ // 쿼리 결과로 1행 이상 존재한다면
            $isManager = false;
            if($row = mysqli_fetch_assoc($result)) {
                $isManager = $row['IsManager'] == 1;
            }
  
            if ($isManager) {
                $sql = "SELECT IsManager FROM USER_GROUP WHERE UserID = ? AND GroupID = ?;";   // 그룹 인원수 가져옴
                $stmt = $conn->prepare($sql);
                $stmt->bind_param("si",$targetUserID,$groupID);
                $stmt->execute();
                $result = $stmt->get_result();
                $row = mysqli_fetch_assoc($result);

                if($row['IsManager'] == 0) {
                    $sql = "DELETE FROM USER_GROUP WHERE UserID = ? AND GroupID = ?;";
                    $stmt = $conn->prepare($sql);
                    $stmt->bind_param("si",$targetUserID,$groupID);
                    $result = $stmt->execute();

                    $sql = "UPDATE PLANT SET GroupID = Null WHERE UserID = ? AND GroupID = ?;";
                    $stmt = $conn->prepare($sql);
                    $stmt->bind_param("si",$targetUserID,$groupID);
                    $result = $stmt->execute();
            
                    if($result === true){
                        //성공 시 동작
                        $outputFrame -> data = 2; // 삭제 완료
                    }
                    else {
                        //실패시 동작
                        $outputFrame -> result = 5;
                        $outputFrame -> message = $stmt->error;
                    }
                }
                else {
                    $outputFrame -> result = 10;
                    $outputFrame -> message = "추방하려는 대상이 그룹 관리자입니다.";
                }
            }
            else {
                $outputFrame -> result = 4;
                $outputFrame -> message = "권한이 없습니다.";
            }
            
        
            $stmt->close();
            mysqli_close($conn);
        }


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
