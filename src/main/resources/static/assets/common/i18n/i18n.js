var I18N = {
    currentLang : null,
    pageLanguage : null,
    init: function () {
        // set change language
        I18N.currentLang = I18N.getCookie()===""?"my":I18N.getCookie();
        $("#changeLanguage").val(I18N.currentLang);
        function formatState (state) {
          if (!state.id) { return state.text; }
          var langEl = state.element.value.toLowerCase();
          var flagSvg = RS[langEl+'FlagSvg'];
            return $(flagSvg);
        };
        $select = $("#changeLanguage").select2({
          templateResult: formatState,
          templateSelection: formatState,
          minimumResultsForSearch: Infinity,
        });

        $select.data('select2').$container.addClass('select2-flag-container');
        $select.data('select2').$dropdown.addClass('select2-flag-dropdown');

        I18N.loadLanguage(I18N.currentLang);
        $('#changeLanguage').change(function() {
            var lang =  $(this).val();
            I18N.currentLang = $(this).val();
            I18N.loadLanguage(lang);
            I18N.saveCookie(lang);
        });

        // load datatable json
        $.getJSON(`/assets/common/i18n/datatable_language_ko.json`,function(langJson) {
            I18N.datatable_lang.ko = langJson;
        });
        $.getJSON(`/assets/common/i18n/datatable_language_my.json`,function(langJson) {
            I18N.datatable_lang.my = langJson;
        })
        $.getJSON(`/assets/common/i18n/datatable_language_en.json`,function(langJson) {
          I18N.datatable_lang.en = langJson;
      })
    },
    loadLanguage: function(language) {

        /* begin :: common items*/
        var asideMenuItems = LANG_ASIDE_MENU[language];
        if(asideMenuItems) {
            I18N.setLanguage(asideMenuItems);
        };
        var headerItems = LANG_HEADER[language];
        if(headerItems) {
            I18N.setLanguage(headerItems);
        };
        $('input[data-ranger-picker="date-ranger-picker"], div[data-ranger-picker="date-ranger-picker"]').each(function(i, obj) {
            if(!$(obj)?.data('daterangepicker')) {
                return;
            }
            $(obj).data('daterangepicker').locale = {
                ...$(obj).data('daterangepicker').locale,
                daysOfWeek: UTL.daysOfWeedKrEn(),
                monthNames: UTL.monthNames(),
                cancelLabel: UTL.clearLabel(),
                applyLabel: UTL.applyLabel()
            };
            $(obj).data('daterangepicker').updateView();
        });
        /* end :: common items*/

        /* logo */
        if($('.logo-mworker').length > 0) {
             var logoSrc = $('.logo-mworker').attr(language);
            $('.logo-mworker').attr('src',logoSrc);
        }
        /* end :: logo*/

        /** begin :: page items */
        I18N.loadPageLanguage(I18N.currentLang);
        /** end :: page items */
    },
    loadPageLanguage: function(language) {
        if(!language) {
            language = I18N.currentLang;
        }
        if(I18N.pageLanguage) {
            var pageItems = I18N.pageLanguage[language];
            I18N.setLanguage(pageItems);
        }

        // select time
        $('.select-time-group label').each(function(i, obj) {
           var keyLang = $(obj).attr('data-i18n-item');
           $(obj).html(LANG_SEARCH_REGISTAION[I18N.currentLang][keyLang])
        });

        // select option
        $('.select-master option').each(function(i, obj) {
            var text = $(obj).attr(I18N.currentLang);
            if(text) {
                $(obj).html(text);
            }
        });
        // error bellow input
        $("div.fv-plugins-message-container").each(function (i, obj) {
            if(I18N.currentLang) {
                var text = $(obj).attr(I18N.currentLang);
                if (text) {
                    $(obj).html(text);
                }
            }
        });
        // cell table
        $("table span.cell-langs, .lang-days-of-week").each(function (i, obj) {
            var text = $(obj).attr(I18N.currentLang);
            if (text) {
                $(obj).html(text);
            }
        });

        // reload select2
        if($('.select-master').length > 0) {
            KTApp.initSelect2();
        }
        // select time group
        $('.select-time-group label').each(function(i, obj) {
          var keyLang = $(obj).attr('data-i18n-item');
          $(obj).html(LANG_SEARCH_REGISTAION[I18N.currentLang][keyLang])
        });

        // update information
        $('.update-information label[data-i18n-item]').each(function(i, obj) {
          var keyLang = $(obj).attr('data-i18n-item');
          $(obj).html(UPDATE_INFORMATION_SECTION[I18N.currentLang][keyLang])
        })

        // update history table
        $('table.UPDATE_HISTORY_TBL th').each(function(i, obj) {
          var keyLang = $(obj).attr('data-i18n-item');
          $(obj).html(UPDATE_HISTORY_TBL[I18N.currentLang][keyLang])
        });

        I18N.fnc_changeLanguageDatatable();

        // span custom
        $(`span[data-i18n-item][${I18N.currentLang}]`).each(function(i, obj) {
          var text = $(obj).attr(I18N.currentLang);
          if (text) {
              $(obj).html(text);
          }
        });

        // set language for fullcalendar
        if (typeof(fullCalendar) !== 'undefined') {
          fullCalendar.setOption("locale", I18N.currentLang === "ko" ? "ko" : "en");
          fullCalendar.setOption("buttonText", FULL_CALENDAR_LANG[I18N.currentLang].buttonText);
        }

        // set lang attribute to content
        $('#ovr_content').attr('language', language);

        KTApp.initBootstrapTooltips();
    },
    setLanguage: function(items) {
        if(!items) {
            console.warn("error");
            return;
        }
        for (const [key, value] of Object.entries(items)) {
          var element = $("[data-i18n-item="+key+"]");
          var elementTagName = element.prop("tagName");
          if(elementTagName === 'SELECT') {
             continue;
          }
          if(elementTagName === 'INPUT') {
           /*  if(element.attr('data-ranger-picker') === 'date-ranger-picker') {
                UTL.fnc_loadDateCalendar(element);
             }*/
             element.attr('placeholder', value);
             continue;
          }

          if (elementTagName === 'TEXTAREA') {
            element.attr('placeholder', value);
             continue;
          }

          if (elementTagName === 'SPAN' && element.attr(I18N.currentLang)) {
            element.html(element.attr(I18N.currentLang));
            continue;
          }
          element.html(value);
        }
    },
    saveCookie: function(cvalue) {
      const d = new Date();
      const exdays = 365;
      d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
      let expires = "expires="+d.toUTCString();
      document.cookie = "LANG=" + cvalue + ";" + expires + ";path=/";
    },
    getCookie : function() {
      let name = "LANG=";
      let ca = document.cookie.split(';');
      for(let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
          c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
          return c.substring(name.length, c.length);
        }
      }
      return "";
    },
    fnc_changeLanguageDatatable: function() {
      let languangeJson = I18N.datatable_lang[I18N.currentLang];
      $('table.dataTable').each((index, element) => {
          if (!element.id) {
            return;
          }
          var tableSelector = "#" + element.id;
          if (DataTable.isDataTable(tableSelector)) {
              for (var field in languangeJson) {
                  let oLanguageObj = $(tableSelector).DataTable().settings()[0].oLanguage;
                  let hungarianMapField = oLanguageObj._hungarianMap[field];
                  if (hungarianMapField && hungarianMapField !== "oPaginate") {
                      $(tableSelector).DataTable().settings()[0].oLanguage[hungarianMapField] =
                          languangeJson[field];
                  }
              }
             $("div.dataTables_processing").html(languangeJson.processing);

             CMTBL.fnc_showEmptyMsg(element.id);
          }
      });
  },
  datatable_lang : {
      ko: null,
      my: null
  }
}