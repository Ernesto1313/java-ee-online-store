<%@ page language="java" contentType="text/plain;charset=UTF-8" pageEncoding="UTF-8"%>
<%
    Integer codigo = (Integer) session.getAttribute("codigo");
    if (codigo != null && codigo > 0) {
        out.print("true");
    } else {
        out.print("false");
    }
%>
