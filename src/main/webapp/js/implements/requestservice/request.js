$(function() {
    
   if ($('#verifyUser').length > 0) {
        $('#verifyUser').on('show.bs.modal', function(e) {
            var targ = e.relatedTarget;
            objFormSearch=$(targ).parent().parent().parent().parent().find('[name=search]');
            
            mObjForm=$(this).find('#validateUser');
            mObjForm.find('.alert').addClass('hidden alert-success');
            targAction=$(targ).attr('href');
            aAction=targAction.split('#')[1];
            
            if(objFormSearch.find('#folio').val()!=='' ){
                eventsAcction();
            }else{
                if(aAction==='services/send_request.json'){
                    eventsAcction();
                }else{
                    return false;
                }
            }
            function eventsAcction(){
                mObjForm[0].reset();
                mObjForm.find('#btnacept').unbind('click');
                mObjForm.find('#btnacept').on("click", function() {
                    eventsModal();
                });


                mObjForm.find('input').unbind('keypress');
                mObjForm.find('input').keypress(function( event ) {
                    if ( event.which === 13 ) {
                        event.preventDefault();
                       eventsModal();
                    }
                });
            };
            
            function eventsModal(){
                dataForm = mObjForm.serializeArray();
                $.post(mObjForm.attr("action"),dataForm,function(data) {
                    if (data.success !== 'undefined' && data.success === true ) {
                        tagHiddenHtml = '<input type="hidden" name="token" value="' + data.token + '" />';

                        if(aAction==='service_detail'){
                            tagHiddenHtml += '<input type="hidden" name="folio" value="' + objFormSearch.find('#folio').val() + '" />';
                            var form = $('<form action="' + objFormSearch.attr('action') + '" method="POST">' + tagHiddenHtml + '</form>');
                            $('body').append(form);
                            $(form).submit();
                        }else{
                            objFormSearch=$(targ).parent().parent().parent().parent().parent().find('[name=search]');
                            tagHiddenHtml += '<input type="hidden" name="folio" value="' + objFormSearch.find('#folio').val() + '" />';
                            var form = $('<form action="' + objFormSearch.attr('action') + '" method="POST">' + tagHiddenHtml + '</form>');
                            $('body').append(form);
                            dataForm=$(form).serializeArray();
                            $.post(aAction,dataForm,function(data) {
                                if (data.success !== 'undefined' && data.success === true ) {
                                     mObjForm.find('.alert').removeClass('hidden alert-danger').addClass('alert-success').html(data.message);
                                    $(form).remove();
                                    $('#verifyUser').modal('hide');
                                    
                                    var form = $('<form action="panel" method="GET"><input name="reload" type="hidden"></form>');
                                    $('body').append(form);
                                    $(form).submit();
                                    
                                    $(form).remove();
                                }else{
                                    mObjForm.find('.alert').removeClass('hidden alert-success').addClass('alert-danger').html(data.message);
                                }
                             }, 'json');
                        }

                    }else{
                        mObjForm.find('.alert').removeClass('hidden alert-success').addClass('alert-danger').html(data.message);
                    } 
                }, 'json');
            }
        });
    }
});
