let select_id = 0
$('#payOut-add-form').submit( ( event ) => {
    event.preventDefault();

    const payouttitle = $("#payouttitle").val()
    const payouttype = $("#payouttype").val()
    const payouttotal = $("#payouttotal").val()
    const payoutdetail = $("#payoutdetail").val()


    const obj = {
        payOutTitle: payouttitle,
        payOutType: payouttype,
        payOutTotal: payouttotal,
        payOutDetail: payoutdetail
    }

    if ( select_id != 0 ) {
        // update
        obj["payOut_id"] = select_id;
    }
    $.ajax({
        url: './payout-post',
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
            alert("İşlem işlemi sırısında bir hata oluştu!");
        }
    })
})

function allPayOut() {

    $.ajax({
        url: './payout-get',
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
        const type =  itm.payOutType == "1" ? 'Nakit' : itm.payOutType == "2" ? 'Kredi Kartı' : itm.payOutType == "3" ? 'Havalet' : itm.payOutType == "4" ? 'EFT' : 'Banka Çeki'
        html += `<tr role="row" class="odd">
            <td>`+itm.payOut_id+`</td>
            <td>`+itm.payOutTitle+`</td>
            <td>`+type+`</td>
            <td>`+itm.payOutTotal+`</td>
            <td>`+itm.payOutDetail+`</td>
            <td class="text-right" >
              <div class="btn-group" role="group" aria-label="Basic mixed styles example">
                <button onclick="fncPayOutDelete(`+itm.payOut_id+`)" type="button" class="btn btn-outline-primary "><i class="far fa-trash-alt"></i></button>
                <button onclick="fncPayOutDetail(`+i+`)" data-bs-toggle="modal" data-bs-target="#payOutDetailModel" type="button" class="btn btn-outline-primary "><i class="far fa-file-alt"></i></button>
                <button onclick="fncPayOutUpdate(`+i+`)" type="button" class="btn btn-outline-primary "><i class="fas fa-pencil-alt"></i></button>
              </div>
            </td>
          </tr>`;
    }
    $('#tableRow').html(html);
}
allPayOut();
// reset fnc
function fncReset() {
    select_id = 0;
    $('#payOut-add-form').trigger("reset");
    allPayOut();
}

function fncPayOutDelete( payOut_id ) {
    let answer = confirm("Silmek istediğinizden emin misniz?");
    if ( answer ) {

        $.ajax({
            url: './payout-delete?payOut_id='+payOut_id,
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

function fncPayOutDetail(i) {
    const itm = globalArr[i];
    $("#payOutTitle").text(itm.payOutTitle +  " - " + itm.payOut_id);
    $("#payOutType").text(itm.payOutType == "" ? '------' : itm.payOutType == "1" ? 'Nakit' : itm.payOutType == "2" ? 'Kredi Kartı' : itm.payOutType == "3" ? 'Havale' : itm.payOutType == "4" ? 'EFT' : 'Banka Çeki');
    $("#payOutTotal").text(itm.payOutTotal == "" ? '------' : itm.payOutTotal);
    $("#payOutDetail").text(itm.payOutDetail == "" ? '------' : itm.payOutDetail);
}

function fncPayOutUpdate( i ) {
    const itm = globalArr[i];
    select_id = itm.payOut_id
    $("#payOutTitle").val(itm.payOutTitle)
    $("#payOutType").val(itm.payOutType)
    $("#payOutTotal").val(itm.payOutTotal)
    $("#payOutDetail").val(itm.payOutDetail)

}
$("#search").keyup(function () {
    allPayOutSearch();
    function allPayOutSearch() {
        const search = $("#search").val();
        $.ajax({
            url: './payout-search-get?search='+search,
            type: 'GET',
            dataType: 'Json',
            success: function (data) {
                createRow2(data);
                console.log(data)
            },
            error: function (err) {
                console.log(err)
            }

        })
        allPayOut()
    }
    function createRow2( data ) {
        globalArr = data;
        let html = ``
        for (let i = 0; i < data.length; i++) {
            const itm = data[i];
            const type =  itm.payOutType == "1" ? 'Nakit' : itm.payOutType == "2" ? 'Kredi Kartı' : itm.payOutType == "3" ? 'Havalet' : itm.payOutType == "4" ? 'EFT' : 'Banka Çeki'
            html += `<tr role="row" class="odd">
            <td>`+itm.payOut_id+`</td>
            <td>`+itm.payOutTitle+`</td>
            <td>`+type+`</td>
            <td>`+itm.payOutTotal+`</td>
            <td>`+itm.payOutDetail+`</td>
          </tr>`;
        }
        $('#tableRow').html(html);
    }


})

$('#search').on("change", function (){
    allPayOut(this.value)
})

