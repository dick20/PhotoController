<%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><?xml version="1.0" encoding="UTF-8"?>
<videonews><c:forEach items="${videos}" var="video">
    <data>
        <wifimessage>${video.wifimessage}</wifimessage>
        <pictureUrl>${video.pictureUrl}</pictureUrl>
    </data></c:forEach>
</videonews>