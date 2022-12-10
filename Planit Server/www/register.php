<?php header("Content-Type:text/html;charset=utf-8"); ?>
<?php
    $userid = $_POST['userID'];
    //$passwordlow=hash("sha256",$_POST['password']);
    $pass=$_POST['password'];
    $email = $_POST['email'];
    $username = $_POST['username'];
    $sex = $_POST['sex'];
    $phonenum = $_POST['phonenum'];
    $birthyear = $_POST['birthyear'];
    $birthmonth = $_POST['birthmonth'];
    $birthday = $_POST['birthday'];
    $nickname = $_POST['nickname'];
    $introduction = $_POST['introduction'];
    $interest = $_POST['interest'];


    $host = 'localhost';
    $user = 'planit2022';
    $password = '------';
    $dbName = 'planit2022';

    $conn = new mysqli($host, $user, $password, $dbName);

    if ($conn -> connect_errno) {
      echo "Failed to connect to MySQL: " . $conn -> connect_error;
      exit();
    }
        
    //최종 출력할 결과
    $outputFrame = new stdClass;
    $outputFrame -> result = 0;             //오류 코드: 0 -> 정상, 나머지 오류는 임의배정
    $outputFrame -> message = "success";    //오류 메세지: 각 오류 상황에서 재설정
    //$sql='CALL register($userid, $password, $email, $username, $sex, $phonenum, $birthyear, $birthmonth, $birthday, $nickname, $introduction, $interest, @resultM, @resultI)'; 
    $sql='CALL register(?,?,?,?,?,?,?,?,?,?,?,?, @resultM, @resultI)'; 
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ssssssssssss",$userid, $pass, $email, $username, $sex, $phonenum, $birthyear, $birthmonth, $birthday, $nickname, $introduction, $interest);
    $stmt->execute();
    //$conn->query($sql);
    
    
    $result=$conn->query('SELECT @resultM, @resultI');
    $row=$result->fetch_assoc();
    
    if($row){
        $outputFrame -> message = $row['@resultM']; //예외 메세지
        $outputFrame -> result = (int)$row['@resultI']; //1:아이디중복, 2:닉네임중복
    
    }
    

    
    mysqli_close($conn);
    echo json_encode($outputFrame);
?>
