<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
	<head>
		<%@include file="base-head.jsp"%>
	</head>
	
	<body>
		<%@include file="nav-menu.jsp"%>
		
		<div id="container" class="container-fluid">
			<h3 class="page-header">Adicionar Venda</h3>

			<form action="${pageContext.request.contextPath}/venda/${action}" method="POST">
				<input type="hidden" value="${venda.getId()}" name="vendaId">
				<div class="row">
					<div class="form-group col-md-4">
						<label for="content">Tipo da Venda</label>
						<input type="text" class="form-control" id="tipo" name="tipo" 
							   autofocus="autofocus" placeholder="Tipo de Venda" 
							   required oninvalid="this.setCustomValidity('Por favor, informe se a venda foi a prazo ou a vista.')"
							   oninput="setCustomValidity('')"
							   value="${venda.getTipo()}">
					</div>
	
					<div class="form-group col-md-4">
						<label for="content">Setor</label>
							<input type="text" class="form-control" id="setor" name="setor" 
								   placeholder="Setor da Venda" 
								   required oninvalid="this.setCustomValidity('Por favor, informe onde a compra foi feita.')"
								   oninput="setCustomValidity('')"
								   value="${venda.getSetor()}">
					</div>
					
					
					<div class="form-group col-md-4">
						<label for="user">Vendedor</label>
						<select id="user" class="form-control selectpicker" name="user" 
							    required oninvalid="this.setCustomValidity('Por favor, informe o usuário pelo qual vc foi atendido.')"
							    oninput="setCustomValidity('')">
						  <option value="" disabled ${not empty venda ? "" : "selected"}>Selecione um usuário</option>
						  <c:forEach var="user" items="${users}">
						  	<option value="${user.getId()}"  ${venda.getUser().getId() == user.getId() ? "selected" : ""}>
						  		${user.getName()}
						  	</option>	
						  </c:forEach>
						</select>
					</div>
				</div>
				<hr/>
				<div class="row">
					<div class="form-group col-md-6">
						<label for="content">Data Compra</label>
						<input type="date" class="form-control" id="start" name="start" 
							   autofocus="autofocus" placeholder="Data da Compra " 
							   required oninvalid="this.setCustomValidity('Por favor, informe a data que você comprou o produto.')"
							   oninput="setCustomValidity('')"
							   value="${venda.getStart()}">
					</div>
					
					<div class="form-group col-md-6">
						<label for="content">Data Pagamento</label>
						<input type="date" class="form-control" id="end" name="end" 
							   autofocus="autofocus" placeholder="Data do Pagamento da Compra"
							   value="${venda.getEnd()}">
					</div>
				</div>
				
				<hr />
				<div id="actions" class="row pull-right">
					<div class="col-md-12">
						<a href="${pageContext.request.contextPath}/vendas" class="btn btn-default">Cancelar</a>
						<button type="submit" 
								class="btn btn-primary">${not empty venda ? "Alterar Venda" : "Criar Venda"}
						</button>
					</div>
				</div>
			</form>
		</div>
		
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
	</body>
</html>
