const SGP = {
    fnc_init: () => {
        $(window).on('hashchange', function (e) {
            SGP.fnc_loadPage();
        });
        SGP.fnc_loadPage();
    },
    fnc_checkLocationHash: (locationHash) => {
        if(['', '/'].includes(locationHash))
            return false;
        if(!locationHash.startsWith('#'))
            return false;
        if(locationHash.endsWith('#'))
            return false;
        if(locationHash.endsWith('#/'))
            return false;
        if((locationHash.match(/#/g)||[]).length > 1)
            return false;
        return true;
    },
    fnc_loadPage: () => {
        let locationHash = window.location.hash;
        if(!SGP.fnc_checkLocationHash(locationHash))
            return;
        SGP.fnc_loadPageByLocationHash(locationHash);
    },
    fnc_loadPageByLocationHash: (locationHash) => {
        var contentPath = locationHash.replace('#', '');
        $.ajax({
            type: "GET"
            ,url: CONTEXT_PATH+contentPath
            ,dataType: "text"
            ,headers: {
                 'PageContent':'PageContent',
             }
            ,success: function(data) {
                $('#pageContent').html(data);
            }
            ,error: function(xhr) {
                // if (xhr.status === 404) {
                //     ALERT.pop_message(MSG.EM059[I18N.currentLang]).then(() => {
                //         const urlA = new URL(document.referrer);
                //         const urlB = new URL(location.href);
                //         if (urlA.origin === urlB.origin) {
                //             history.back();
                //             return;
                //         }
                //     });
                //     return;
                // }
            }
            ,complete: function(){
                $('#overlay').hide();
            }
        });
    },
}