<?php header("Content-Type:text/html;charset=utf-8"); ?>
<?php
    require __DIR__ . '/../vendor/autoload.php';
    use Firebase\JWT\JWT;
    use Firebase\JWT\Key;
    $secretkey = '------';

    $id = $_POST['userID'];
    $pass = $_POST['userPass'];

    $host = 'localhost';
    $user = 'planit2022';
    $password = '------';
    $dbName = 'planit2022';

    header('Content-Type: application/json');

    $conn = new mysqli($host, $user, $password, $dbName);
    if(!($conn)){
        echo "db 연결 실패: " . mysqli_connect_error();
    }

    $sql = "SELECT Password, NickName FROM USER WHERE UserID = ?;";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s",$id);
    $stmt->execute();
    $result = $stmt->get_result();
    $output = new stdClass;

    if(mysqli_num_rows($result) > 0){ // 쿼리 결과로 1행 이상 존재한다면
        if($row = mysqli_fetch_assoc($result)) {
            if($pass === $row['Password']) {
                //비밀번호 일치
                $output -> result = 0;
                $output -> userID = $id;
                $output -> nickName = $row['NickName'];

                //jwt
                $payload = [
                    'iss' => 'http://planit2022.cafe24.com',
                    'aud' => 'planitApp',
                    'userID' => $id,
                    'userName' => $row['NickName']
                ];
                $jwt = JWT::encode($payload, $secretkey, 'HS256');
                header("Authenticate: $jwt");
            }
            else {
                $output -> result = 2; // 비번 틀림
            }
        }
    } else{
        $output -> result = 1; // 아이디 존재X
    }

    echo json_encode($output);
    $stmt->close();
    mysqli_close($conn);
?>
