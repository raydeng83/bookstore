$(document).ready(function() {
    $('.delete-book').on('click', function (e) {
        /*<![CDATA[*/
        var path = /*[[@{/}]]*/'remove';
        /*]]>*/
        var id= $(this).attr('id');

        bootbox.confirm({
            message: "Are you sure to remove this book? It can't be undone.",
            buttons: {
                cancel: {
                    label: '<i class="fa fa-times"></i> Cancel'
                },
                confirm: {
                    label: '<i class="fa fa-check"></i> Confirm'
                }
            },
            callback: function (confirmed) {
                if(confirmed) {
                    $.post(path, {'id': id}, function (res) {
                        location.reload();
                    });
                }
            }
        });
    });

    var bookIdList=[];

    $('.checkboxBook').click(function () {
        var id = $(this).attr('id');
        if($(this).prop("checked") == true){
            bookIdList.push(id);
        }
        else if($(this).prop("checked") == false){
            bookIdList.splice(bookIdList.indexOf(id), 1);
        }
    })

    $('#deleteSelected').click(function(){
        /*<![CDATA[*/
        var path = /*[[@{/}]]*/'removeList';
        /*]]>*/

        bootbox.confirm({
            message: "Are you sure to remove all selected books? It can't be undone.",
            buttons: {
                cancel: {
                    label: '<i class="fa fa-times"></i> Cancel'
                },
                confirm: {
                    label: '<i class="fa fa-check"></i> Confirm'
                }
            },
            callback: function (confirmed) {
                if(confirmed) {
                    // $.post(path, JSON.stringify(bookIdList), function (res) {
                    //     location.reload();
                    // });

                    $.ajax({
                        type: 'POST',
                        url: path,
                        data: JSON.stringify(bookIdList),
                        contentType: "application/json",
                        success: function(res) { console.log(res); location.reload(); },
                        error: function(res) {
                            console.log(res);
                            location.reload();
                        }
                    });
                }
            }
        });
    });
});