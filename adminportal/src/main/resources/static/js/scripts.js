$(document).ready(function() {
    $('.delete-book').on('click', function (e) {
        /*<![CDATA[*/
        var path = /*[[@{/}]]*/'remove';
        /*]]>*/
        var id= $(this).attr('id');

        bootbox.confirm({
            title: "Destroy planet?",
            message: "Do you want to activate the Deathstar now? This cannot be undone.",
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
});