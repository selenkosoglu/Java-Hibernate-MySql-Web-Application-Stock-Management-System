let select_id = 0
$('#products_add_form').submit( ( event ) => {
    event.preventDefault();

    const ptitle = $("#ptitle").val()
    const aprice = $("#aprice").val()
    const oprice = $("#oprice").val()
    const pcode = $("#pcode").val()
    const ptax = $("#ptax").val()
    const psection = $("#psection").val()
    const size = $("#size").val()
    const pdetail = $("#pdetail").val()

    const obj = {
        p_title: ptitle,
        p_purchase_price: aprice,
        p_sale_price: oprice,
        p_code: pcode,
        p_kdv: ptax,
        p_unit: psection,
        p_amount: size,
        p_detail: pdetail,
    }

    if ( select_id != 0 ) {
        // update
        obj["p_id"] = select_id;
    }
    $.ajax({
        url: './products-post',
        type: 'POST',
        data: { obj: JSON.stringify(obj) },
        dataType: 'JSON',
        success: function (data) {
            if ( data > 0 ) {
                alert("İşlem Başarılı")
                fncReset();
            }else {
                alert("İşlem sırasında hata oluştu!");
            }
        },
        error: function (err) {
            console.log(err)
            alert("İşlem sırısında bir hata oluştu!");
        }
    })
})

// all products list - start
function allProducts() {
    $.ajax({
        url: './products-get',
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            createRow(data);
        },
        error: function (err) {
            console.log(err)
        }
    })
}
let globalArr = []
function createRow( data ) {
    globalArr = data;
    let html = ``
    for (let i = 0; i < data.length; i++) {
        const itm = data[i];
        const kdv = itm.p_kdv == 1 ? 'Dahil' : itm.p_kdv == 2 ? '%1' : itm.p_kdv == 3 ?  '%8': '%18'
        const unit = itm.p_unit == 1 ? 'Adet' : itm.p_unit == 2 ? 'KG' : itm.p_unit == 3 ?  'Metre':  itm.p_unit == 4 ? 'Paket': 'Litre'
        html += `<tr role="row" class="odd">
            <td>`+itm.p_id+`</td>
            <td>`+itm.p_title+`</td>
            <td>`+itm.p_purchase_price+`</td>
            <td>`+itm.p_sale_price+`</td>
            <td>`+itm.p_code+`</td>
            <td>`+ kdv +`</td>
            <td>`+ unit +`</td>
            <td>`+itm.p_amount+`</td>
            <td>`+itm.p_detail+`</td>
            <td class="text-right" >
              <div class="btn-group" role="group" aria-label="Basic mixed styles example">
                <button onclick="fncProductsDelete(`+itm.p_id+`)" type="button" class="btn btn-outline-primary "><i class="far fa-trash-alt"></i></button>
                <button onclick="fncProductDetail(`+i+`)" data-bs-toggle="modal" data-bs-target="#productsDetailModel" type="button" class="btn btn-outline-primary "><i class="far fa-file-alt"></i></button>
                <button onclick="fncProductUpdate(`+i+`)" type="button" class="btn btn-outline-primary "><i class="fas fa-pencil-alt"></i></button>
              </div>
            </td>
          </tr>`;
    }
    $('#tableRow').html(html);
}

function codeGenerator() {
    const date = new Date();
    const time = date.getTime();
    const key = time.toString().substring(4);
    $('#ccode').val( key )
    $('#pcode').val( key )
}

allProducts();

// reset fnc
function fncReset() {
    select_id = 0;
    $('#products_add_form').trigger("reset");
    codeGenerator();
    allProducts()
}

// product delete - start
function fncProductsDelete( p_id ) {
    let answer = confirm("Silmek istediğinizden emin misniz?");
    if ( answer ) {

        $.ajax({
            url: './products-delete?p_id='+p_id,
            type: 'DELETE',
            dataType: 'text',
            success: function (data) {
                if ( data != "0" ) {
                    fncReset();
                }else {
                    alert("Silme sırasında bir hata oluştu!");
                }
            },
            error: function (err) {
                console.log(err)
            }
        })
    }
}

// product detail - start
function fncProductDetail(i){
    const itm = globalArr[i];
    $("#p_title").text(itm.p_title.toUpperCase() + " - " + itm.p_id);//item içindeki cu_namei jspde cu_name e atar
    $("#p_purchase_price").text(itm.p_purchase_price == "" ? '------' : itm.p_purchase_price);
    $("#p_sale_price").text(itm.p_sale_price == "" ? '------' : itm.p_sale_price);
    $("#p_code").text(itm.p_code == "" ? '------' : itm.p_code);
    $("#p_kdv").text(itm.p_kdv ==1 ? 'Dahil' : itm.p_kdv == 2 ? '%1' : itm.p_kdv == 3 ?  '%8': '%18');
    $("#p_unit").text(itm.p_unit ==  1 ? 'Adet' : itm.p_unit == 2 ? 'KG' : itm.p_unit == 3 ?  'Metre':  itm.p_unit == 4 ? 'Paket': 'Litre');
    $("#p_amount").text(itm.p_amount == "" ? '------' : itm.p_amount);
    $("#p_detail").text(itm.p_detail == "" ? '------' : itm.p_detail);
}


// product update select
function fncProductUpdate( i ) {
    const itm = globalArr[i];
    select_id = itm.cu_id
    $("#ptitle").val(itm.p_title)
    $("#aprice").val(itm.p_purchase_price)
    $("#oprice").val(itm.p_sale_price)
    $("#pcode").val(itm.p_code)
    $("#ptax").val(itm.p_kdv)
    $("#psection").val(itm.p_unit)
    $("#size").val(itm.p_amount)
    $("#pdetail").val(itm.p_detail)
}
