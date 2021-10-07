let select_id = 0
let amount;
$('#boxAction_add_form').submit( ( event ) => {
    event.preventDefault();

    const cname = $("#cname").val()
    const pname = $("#pname").val()
    const count = $("#count").val()
    const bNo = $("#bNo").val()

    const obj = {
        o_customer: cname,
        o_product: pname,
        o_amount: count,
        o_receipt_number: bNo,
        box_status: 0
    }

    if ( select_id != 0 ) {
        // update
        obj["o_id"] = select_id;
    }
    $.ajax({
        url: './orders-post',
        type: 'POST',
        data: { obj: JSON.stringify(obj) },
        dataType: 'JSON',
        success: function (data) {
            if ( data > 0 ) {
                alert("İşlem Başarılı")
                allOrders(cname)
            }else {
                alert("İşlem sırasında hata oluştu!");
            }
        },
        error: function (err) {
            console.log(err)
            alert("İşlem sırasında bir hata oluştu!");
        }
    })
})

function allOrders(cid) {

    $.ajax({
        url: './orders-get?cid='+cid,
        type: 'GET',
        dataType: 'JSON',
        success: function (data) {
            createRow(data);
        },
        error: function (err) {
            console.log(err)
        }
    })
}


let globalArr = []
let receipt_no = 0;
function createRow( data ) {
    console.log(data)
    globalArr = data;

    let html = ``
    for (let i = 0; i < data.length; i++) {
        const itm = data[i];
        let fullName = itm.cu_name+" "+itm.cu_surname;
        html += `<tr role="row" class="odd">
            <td>`+itm.o_id+`</td>
            <td>`+itm.p_title+`</td> <!-- ürün -->
            <td>`+itm.p_sale_price+`</td> <!-- fiyat -->
            <td>`+itm.o_amount+`</td>
            <td>`+fullName+`</td> <!-- müşteri -->
            <td>`+itm.o_receipt_number+`</td>
            <td class="text-right" >
              <div class="btn-group" role="group" aria-label="Basic mixed styles example">
                <button onclick="fncOrdersDelete(`+itm.o_id+`)" type="button" class="btn btn-outline-primary "><i class="far fa-trash-alt"></i></button>
              </div>
            </td>
          </tr>`;
    }
    if (globalArr.length > 0) {
        receipt_no = globalArr[0].o_receipt_number
        console.log(receipt_no)
    }
    $('#tableRow').html(html);
}


$('#cname').on("change", function (){
    allOrders(this.value)
})



// reset fnc
function fncReset() {
    select_id = 0;
    $('#boxAction_add_form').trigger("reset");
    allOrders(select_id);
}



// orders delete
function fncOrdersDelete( o_id ) {
    const cname = $("#cname").val()
    let answer = confirm("Silmek istediğinizden emin misniz?");
    if ( answer ) {

        $.ajax({
            url: './orders-delete?o_id='+o_id,
            type: 'DELETE',
            dataType: 'text',
            success: function (data) {
                if ( data != "0" ) {
                    allOrders(cname)
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

$(document).on('click', '#completeOrderButton', function (data) {
    alert("Button tıklandı")
    const objx = {
        o_receipt_number: receipt_no
    }
    $.ajax({
        url: './receipt-post',
        type: 'POST',
        data: {objx: JSON.stringify(objx)},
        dataType: 'JSON',
        success: function (data) {
            if (data > 0) {
                alert("Sipariş tamamlandı")
                allOrders($("#cname").val())

            } else {
                alert("İşlem sırasında hata oluştu!");
            }
        },
        error: function (err) {
            console.log(err)
            alert("İşlem sırasında hata oluştu!")
        }
    })


})

//ADET AYARI
$('#pname').change(function () {
    selectedProduct = this.value;
    if (selectedProduct != 0) {
        fncAmount();
    }
})

function fncAmount() {
    $.ajax({
        url: './amount-servlet?selectedProduct=' + selectedProduct,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            console.log(data)
            amount = data;
            maxAmount();//Max girilebilir adet ayarlama.
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function maxAmount() {
    $("#count").val(amount.p_amount);
    $("#count").attr('max', amount.p_amount);
    maxNumber = amount.p_amount;
    if (maxNumber == 0) {
        $("#count").attr('min', 0)
    }
}

//Ürün Adeti > 0
$("#count").keyup(function () {
    const value = $("#count").val();
    if (value < 1) {
        $("#count").val(1);
    } else if (value > maxNumber) {
        $("#count").val(maxNumber);
    }
})
