$(function () {
     var  initUrl ='/shop/getshopinitinfo';
     var registersShopUrl='/shop/registershop';
     //alert(initUrl);
     getShopInitInfo();
     function getShopInitInfo() {
         $.getJSON(initUrl,function (data){
             if(data.success){
                 var tempHtml='';
                 var tempAreaHtml='';
                 data.shopCategoryList.map(function (item, index) {
                     tempHtml+='<option data-id="'+item.shopCategoryId+'">'+item.shopCategoryName+'</option>';
                 });
                 data.areaList.map(function (item, index) {
                     tempAreaHtml+='<option data-id="'+item.areaId+'">'+item.areaName+'</option>';
                 });
                 $('#shop_category').html(tempHtml);
                 $('#area').html(tempAreaHtml);
             }
         });
        $('#submit').click(function () {
             var shop={};
             shop.shopName=$('#shop_name').val();
             shop.shopAddr=$('#shop_addr').val();
             shop.phone=$('shop_phone').val();
             shop.shopDesc=$('shop_desc').val();
             shop.shopCategory={
                 shopCategoryId :$('#shop_category').find('option').not(function () {
                     return !this.selected;
                 }).data(id)
             };
             shop.area={
                 areaId:$('#area').find('option').not(function () {
                     return !this.selected;
                 }).data(id)
             };
             var shopImg=$('#shop_img')[0].files[0];
             var formData=new FormData();
             formData.append('shopImg',shopImg);
             formData.append("shopstr",JSON.stringify(shop));
             $.ajax({
                url:registersShopUrl,
                type:'POST',
                data:formData,
                contentType:false,
                processData:false,
                cache:false,
                success:function (data) {
                    if(data.success){
                        $.toast('提交成功！');
                    }else{
                        $.toast('提交失败！'+data.errMsg);
                    }
                }
             });
        });
     }
})