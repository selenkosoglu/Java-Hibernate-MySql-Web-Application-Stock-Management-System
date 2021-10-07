let selectedVoucher;
let paymentTotal = [];
let maxNumber;
let customerSelect;
let globalxVoucher;
let select_id = 0

$("#cid").change( function (){
    getCuReceipt(this.value)
    customerSelect = this.value;
    allPayIn()
})

function getCuReceipt(cid) {

    $.ajax({
        url: './customer-receipt-get?cid=' + cid,
        type: 'GET',
        dataType: 'JSON',
        success: function (data) {
            setReceipt(data)
            paymentTotal = data;
        },
        error: function (err) {
            console.log(err)
        }
    })
}
function setReceipt(data){
    $("#rname").find('option').remove();
    $('#rname').append("<option value=" + 0 + " data-subtext=" + ">Seçim Yapınız</option>")

    for (let i = 0; i < data.length; i++) {
        const item = data[i];
        $('#rname').append("<option value="+item.o_receipt_number+" data-subtext= " + item.receipt_total + ">" + item.o_receipt_number + "</option>");
    }

    $('#rname').val(0);
    $('#rname').selectpicker("refresh");
    selectedVoucher = 0;

}
$("#rname").change( function (){
    selectedVoucher = this.value
    console.log(selectedVoucher)
    changeMaxNumber();
})


function changeMaxNumber(){
    if (selectedVoucher > 0) {
        $.ajax({
            url: './receipt-get?cid=' + customerSelect + '&o_receipt_number=' + selectedVoucher,
            type: 'GET',
            dataType: 'JSON',
            success: function (data) {
                if (data) {
                    globalxVoucher = data;
                    console.log(data)
                    changeMaxNumberContinue(1);
                } else {
                    alert("Boş giriş mevcut...")
                }
            },
            error: function (err) {
                console.log(err)
            }
        })
    } else {
        changeMaxNumberContinue(0)
    }
}
function changeMaxNumberContinue(x) {
    if (x == 1) {
        const data = globalxVoucher;
        maxNumber = parseInt(data.receipt_total) - parseInt(data.receipt_payment);
        $("#payInTotal").attr("max", maxNumber);
        $("#payInTotal").val(maxNumber);
    } else if (x == 0) {
        globalxVoucher = 0;
        maxNumber = 1;
        $("#payInTotal").attr("max", maxNumber);
        $("#payInTotal").val(maxNumber);
    }

}
//Ödeme Tutarı > 0
$("#payInTotal").keyup(function () {
    $("#payInTotal").attr("min", 1);
    if (maxNumber != 0) {
        $("#payInTotal").attr("max", maxNumber);
    } else {
        $("#payInTotal").attr("max", 1);
    }

    const value = $("#payInTotal").val();
    if (value < 1) {
        $("#payInTotal").val(1);
    } else if (value > maxNumber) {
        $("#payInTotal").val(maxNumber);
    }
})


$('#payIn_add_form').submit( ( event ) => {
    event.preventDefault();

    const pay_in_amount = $("#payintotal").val()
    const pay_in_detail = $("#payindetail").val()
    const customer_id = $("#cid").val()
    const receipt_number = $("#rname").val()


    const obj = {
        payment_amount: pay_in_amount,
        payment_detail: pay_in_detail,
        cu_id : customer_id,
        o_id : receipt_number,
    }
    console.log(obj)
    if ( select_id != 0 ) {
        // update
        obj["payIn_id"] = select_id;
    }
    $.ajax({
        url: './payin-post',
        type: 'POST',
        data: { obj: JSON.stringify(obj) },
        dataType: 'JSON',
        success: function (data) {
            if ( data > 0 ) {
                alert("İşlem Başarılı")
                allPayIn()
            }else {
                alert("İşlem sırasında hata oluştu!");
            }
        },
        error: function (err) {
            console.log(err)
            alert("İşlem işlemi sırısında bir hata oluştu!");
        }
    })

})


function allPayIn() {

    $.ajax({
        url: './payin-get?cid='+customerSelect,
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
    console.log(data)

    let html = ``
    for (let i = 0; i < data.length; i++) {
        const itm = data[i];
        let date = itm.date.date;
        date = date.day + "/" + date.month + "/" + date.year;
        html += `<tr role="row" class="odd">
            <td>`+itm.payIn_id+`</td>
            <td>`+itm.cu_name+`</td>
            <td>`+itm.cu_surname+`</td>
            <td>`+itm.o_receipt_number+`</td>
            <td>`+itm.payment_amount+`</td>
            <td>`+date+`</td>
          </tr>`;
    }
    $('#tableRow').html(html);
}

allPayIn();

// reset fnc
function fncReset() {
    select_id = 0;
    $('#payIn_add_form').trigger("reset");
    allPayIn();

    $('#cid').val(0);
    $('#cid').selectpicker("refresh");
    $('#rname').val(0);
    $('#rname').selectpicker("refresh");

    $('#payInTotal').val("");
    $('#payInDetail').val("");


}
/*
function fncPayInDelete( payIn_id ) {
    let answer = confirm("Silmek istediğinizden emin misniz?");
    if ( answer ) {

        $.ajax({
            url: './payin-delete?payIn_id='+payIn_id,
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

function fncPayInDetail(i) {
    const itm = globalArr[i];
    $("#cid").text(itm.cid +  " - " + itm.payIn_id);
    $("#rname").text(itm.rname == "" ? '------' : itm.rname);
    $("#payInTotal").text(itm.payInTotal == "" ? '------' : itm.payInTotal);
    $("#payInDetail").text(itm.payInDetail == "" ? '------' : itm.payInDetail);
}

function fncPayInUpdate( i) {
    $('#rname').val(i);
    $('#rname').selectpicker("refresh");
    const itm = globalArr[i];
    $("#payintotal").val(itm.pay_in_amount)
    $("#payindetail").val(itm.pay_in_detail)

}
 */
