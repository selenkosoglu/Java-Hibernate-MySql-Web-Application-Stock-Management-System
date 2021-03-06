<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<%@ page import="entities.User" %>
<jsp:useBean id="util" class="utils.Util"></jsp:useBean>
<% User user = util.isLogin(request, response); %>
<html lang="en">

<head>
  <title>Kasa Yönetimi / Ödeme Çıkışı</title>
  <jsp:include page="inc/css.jsp"></jsp:include>
</head>

<body>

<div class="wrapper d-flex align-items-stretch">
  <jsp:include page="inc/sideBar.jsp"></jsp:include>
  <div id="content" class="p-4 p-md-5">
    <jsp:include page="inc/topMenu.jsp"></jsp:include>
    <h3 class="mb-3">
      Kasa Yönetimi
      <small class="h6">Ödeme Çıkışı</small>
    </h3>

    <div class="main-card mb-3 card mainCart">
      <div class="card-header cardHeader">Ödeme Ekle</div>

      <form class="row p-3" id="payOut-add-form">

        <div class="col-md-3 mb-3">
          <label for="payOutTitle" class="form-label">Başlık</label>
          <input type="text" name="payOutTitle" id="payOutTitle" class="form-control" />
        </div>

        <div class="col-md-3 mb-3">
          <label for="payOutType" class="form-label">Ödeme Türü</label>
          <select class="form-select" name="payOutType" id="payOutType">
            <option>Ödeme Türünü Seçiniz</option>
            <option value="1">Nakit</option>
            <option value="2">Kredi Kartı</option>
            <option value="3">Havale</option>
            <option value="4">EFT</option>
            <option value="5">Banka Çeki</option>
          </select>
        </div>
        <div class="col-md-3 mb-3">
          <label for="payOutTotal" class="form-label">Ödeme Tutarı</label>
          <input type="number" name="payOutTotal" id="payOutTotal" class="form-control" />
        </div>

        <div class="col-md-3 mb-3">
          <label for="payOutDetail" class="form-label">Ödeme Detay</label>
          <input type="text" name="payOutDetail" id="payOutDetail" class="form-control" />
        </div>

        <div class="btn-group col-md-3 " role="group">
          <button type="submit" class="btn btn-outline-primary mr-1">Gönder</button>
          <button type="reset" class="btn btn-outline-primary">Temizle</button>
        </div>

      </form>
    </div>


    <div class="main-card mb-3 card mainCart">
      <div class="card-header cardHeader">Ödeme Çıkış Listesi</div>

      <div class="row mt-3" style="padding-right: 15px; padding-left: 15px;">
        <div class="col-sm-3"></div>
        <div class="col-sm-3"></div>
        <div class="col-sm-3"></div>
        <div class="col-sm-3">
          <div class="input-group flex-nowrap">
            <span class="input-group-text" id="addon-wrapping"><i class="fas fa-search"></i></span>
            <input id="search" type="text" class="form-control" placeholder="Arama.." aria-label="Username" aria-describedby="addon-wrapping">
            <button id="payOutSearch" class="btn btn-outline-primary">Ara</button>
          </div>
        </div>



      </div>
      <div class="table-responsive">
        <table class="align-middle mb-0 table table-borderless table-striped table-hover">
          <thead>
          <tr>
            <th>Id</th>
            <th>Başlık</th>
            <th>Ödeme Türü</th>
            <th>Ödeme Detayı</th>
            <th>Ödeme Tutarı</th>
            <th class="text-center" style="width: 155px;" >Yönetim</th>
          </tr>
          </thead>
          <tbody id ="tableRow">
          <!-- for loop  -->

          </tbody>
        </table>
      </div>
    </div>

    <div class="modal fade" id="payOutDetailModel" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg" >
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" style="color: black"   id="payOutTitle">Modal title</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div class="row ">
              <div class="col-md-3 mb-3">
                <label  class="form-label">Ödeme Türü</label>
                <div style="color: black" id="payOutType" class="form-text"></div>
              </div>
              <div class="col-md-3 mb-3">
                <label  class="form-label">Ödeme Tutarı</label>
                <div style="color: black" id="payOutTotal" class="form-text"></div>
              </div>
              <div class="col-md-3 mb-3">
                <label  class="form-label">Ödeme Detay</label>
                <div style="color: black" id="payOutDetail" class="form-text"></div>
              </div>

            </div>

          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary" data-bs-dismiss="modal">Kapat</button>

          </div>
        </div>
      </div>
    </div>




  </div>
</div>
</div>




<jsp:include page="inc/js.jsp"></jsp:include>
<script src="js/payOut.js"></script>
</body>

</html>
