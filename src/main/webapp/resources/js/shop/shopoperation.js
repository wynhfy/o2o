$(function () {
     var shopId=getQueryString("shopId");
     var isEdit=shopId?true:false; //非空返回true,空的返回false
     var initUrl ='/shop/getshopinitinfo';
     var registerShopUrl='/shop/registershop';
     var shopInfoUrl='/shop/getshopbyid?shopId='+shopId;
     var editShopUrl='/shop/modifyshop';
     //alert(initUrl);
     if(!isEdit){
         getShopInitInfo();

     }else{
         getShopInfo(shopId);
     }
     //编辑shop的方法
    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl,function (data){
            if(data.success){
                var shop=data.shop;
                $('#shop_name').val(shop.shopName);
                $('#shop_addr').val(shop.shopAddr);
                $('#shop_desc').val(shop.shopDesc);
                $('#shop_phone').val(shop.phone);
                var shopCategory = '<option data-id="'
                    + shop.shopCategory.shopCategoryId + '" selected>'
                    + shop.shopCategory.shopCategoryName + '</option>';
                var tempAreaHtml='';
                data.areaList.map(function (item, index) {
                    tempAreaHtml+='<option data-id="'+item.areaId+'">'+item.areaName+'</option>';
                });
                $('#shop_category').html(shopCategory);
                $('#shop_category').attr('disabled','disabled');
                $('#area').html(tempAreaHtml);
                $("#area option[data-id='"+shop.area.areaId+"']").attr("selected","selected");
            }
        });
    }

     //注册shop的方法
     function getShopInitInfo() {
         $.getJSON(initUrl, function (data) {
             if (data.success) {
                 var tempHtml = '';
                 var tempAreaHtml = '';
                 data.shopCategoryList.map(function (item, index) {
                     tempHtml += '<option data-id="' + item.shopCategoryId + '">' + item.shopCategoryName + '</option>';
                 });
                 data.areaList.map(function (item, index) {
                     tempAreaHtml += '<option data-id="' + item.areaId + '">' + item.areaName + '</option>';
                 });
                 $('#shop_category').html(tempHtml);
                 $('#area').html(tempAreaHtml);
             }
         });
     }
     $('#submit').click(function () {
             var shop={};
             if(isEdit){
                 shop.shopId=shopId;
             }
             shop.shopName=$('#shop_name').val();
             shop.shopAddr=$('#shop_addr').val();
             shop.phone=$('#shop_phone').val();
             shop.shopDesc=$('#shop_desc').val();
             shop.shopCategory={
                 shopCategoryId :$('#shop_category').find('option').not(function () {
                     return !this.selected;
                 }).data('id')
             };
             shop.area={
                 areaId:$('#area').find('option').not(function () {
                     return !this.selected;
                 }).data('id')
             };
             var shopImg=$('#shop_img')[0].files[0];
             var formData=new FormData();
             formData.append('shopImg',shopImg);
             formData.append("shopstr",JSON.stringify(shop));
             var verifyCodeActual=$('#kaptcha').val();
             if(!verifyCodeActual){
                 $.toast("请输入验证码！");
                 return;
             }
             formData.append("verifyCodeActual",verifyCodeActual);
             $.ajax({
                 url : (isEdit ? editShopUrl : registerShopUrl),
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
                    $('#kaptcha_img').click();
                }
             });
     });

})