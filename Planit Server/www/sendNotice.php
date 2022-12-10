<?php
          // 1 물주기 2 가입 3 거절 4 추방 5 가입신청
        $noticeSql = "INSERT INTO NOTICE(UserID, SenderID, Message, Type, TargetID, Date) VALUES(?, ?, ?, ?, ?, NOW());";
        $noticeStmt = $conn->prepare($noticeSql);
        $noticeStmt->bind_param("sssii",$noticeRecvID, $noticeSendID, $noticeMSG, $noticeType, $noticeTarget); // ?자리에 순서대로 들어감, 첫 매개변수는 이후 나올 매개변수의 자료형 -> 문자열은 s, 정수는 i
        $noticeResult = $noticeStmt->execute();
?>