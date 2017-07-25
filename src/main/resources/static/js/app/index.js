$(function () {
    var base = '/couch-potato/';
    alert("js is working");
    $.ajax({
        type: 'GET',
        url: base + 'itemList'

    }).done(function (data, textStatus, jqXHR) {
        $('#helloweenMessage').html(data.message);

    }).fail(function (jqXHR, textStatus, errorThrown) {
        if (jqXHR.status === 401) { // HTTP Status 401: Unauthorized
            var preLoginInfo = JSON.stringify({method: 'GET', url: base});
            $.cookie('couchpotato.pre.login.request', preLoginInfo);
            window.location = base + 'login.html';

        } else {
            alert('Houston, we have a problem...');
        }
    });
});
