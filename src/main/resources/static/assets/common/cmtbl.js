var CMTBL = {
    table: null,
    dataTablesEmptyClass: "dataTables_empty",
    pageLength: 10,
    lengthMenu: [10, 20, 30, 50],
    stateTable: {},
    dom:
        "<'row'<'col-sm-12 table-responsive min-height-150'tr>>" +
        "<'row'<'col-sm-12 col-md-12 d-flex justify-content-center'p>>",
    checkBoxDom:
        '<div class="form-check form-check-sm form-check-custom d-inline-flex"> <input class="form-check-input" type="checkbox" value=""></div>',
    imageDom: '<img class="ms-3" src="/assets/media/svg/files/image.svg" width="18" height="18" />',
    documentDom:
        '<img class="ms-3" src="/assets/media/svg/files/document.svg" width="18" height="18" />',
    sortDom: '<img  class="sort-icon pe-3" src="/assets/media/svg/files/sort.svg"/>',
    fnc_renderSortDom: function (meta, setByAdmin) {
        let html = '<div class="w-100 d-flex justify-content-between gap-3 px-3">';
        html += `<img class="sort-icon ${
            setByAdmin ? "" : "invisible"
        }" src="/assets/media/svg/files/sort.svg"/>`;
        html += `<span class='align-middle'>${CMTBL.fnc_renderIndex(meta)}</span>`;
        html += "</div>";
        return html;
    },
    fnc_renderIndex: function (meta, isAsc) {
        var serverSide = meta.settings.oInit.serverSide;
        if (serverSide) {
            return this.fnc_renderIndexLazy(meta, isAsc);
        }
        return this.fnc_renderIndexPre(meta, isAsc);
    },
    fnc_renderIndexDummy: function (meta, isAsc, totalPrm) {
        var ascIndex = meta.row + 1;
        if (isAsc) {
            return ascIndex;
        }
        if (!totalPrm) {
            alert("plz add total records");
            return;
        }
        var total = totalPrm;
        var descIndex = total - (ascIndex - 1);
        return descIndex;
    },
    fnc_renderIndexPre: function (meta, isAsc) {
        var ascIndex = meta.row + 1;
        if (isAsc) {
            return ascIndex;
        }
        var total = meta.settings.json.recordsTotal;
        var descIndex = total - (ascIndex - 1);
        return descIndex;
    },
    fnc_renderIndexLazy: function (meta, isAsc) {
        var rowStartIndex = meta.settings.oAjaxData.start;
        var rowPageIndex = meta.row;
        var ascIndex = rowStartIndex + rowPageIndex + 1;
        if (isAsc) {
            return ascIndex;
        }
        var total = meta.settings._iRecordsTotal;
        var descIndex = total - (ascIndex - 1);
        return descIndex;
    },
    fnc_createPageRequest: function (data, settings, isCurrentPage) {
        // page
        var pageSize = settings._iDisplayLength;

        var page = data.start / pageSize + 1;
        // sort
        var sortSqls = data.order.map(function (e) {
            var columnName = data.columns[e.column].data;
            var sortType = e.dir;
            return columnName + " " + sortType;
        });
        var sortSql = sortSqls.join(", ");
        // page request
        var pageRequest = {
            draw: data.draw,
            page: page,
            pageSize: pageSize,
            sortSql: sortSql,
        };
        return pageRequest;
    },
    fnc_getInitStateTable: function (tableId) {
        var initState =
            CMTBL.stateTable[location.hash] &&
            typeof CMTBL.stateTable[location.hash][tableId]?.init !== "undefined"
                ? CMTBL.stateTable[location.hash][tableId]?.init
                : true;
        return initState;
    },
    fnc_getPageSizeStateTable: function (tableId) {
        var pageSize =
            CMTBL.stateTable[location.hash] &&
            typeof CMTBL.stateTable[location.hash][tableId]?.pageSize !== "undefined"
                ? CMTBL.stateTable[location.hash][tableId].pageSize
                : 0;
        return pageSize;
    },
    fnc_saveStateTable: function (tableId, init, pageSize) {
        // save state table
        var currentPageObj = CMTBL?.stateTable?.[location.hash]
            ? CMTBL?.stateTable?.[location.hash]
            : {};
        var pageObj = {
            [location.hash]: {
                ...currentPageObj,
                [tableId]: {
                    init: init,
                    pageSize: pageSize,
                },
            },
        };
        CMTBL.stateTable[location.hash] = pageObj[location.hash];
    },
    fnc_getCheckedRows: function (tableId, chkColIdxPrm) {
        var chkColIdx = 1;
        if (chkColIdxPrm) {
            chkColIdx = chkColIdxPrm;
        }
        var aoData = $("#" + tableId)
            .DataTable()
            .data().context[0].aoData;
        if (aoData && aoData.length > 0) {
            var checkedRows = [];
            aoData.forEach(function (rowData) {
                var chk = $(rowData.anCells).find('input[type="checkbox"]')[chkColIdx - 1];
                if (chk.checked) {
                    checkedRows.push(rowData._aData);
                }
            });
            return checkedRows;
        }
        return [];
    },
    fnc_checkRowsPreload: function (tableId, colIdxPrm) {
        var colIdx = 1;
        if (colIdxPrm) {
            colIdx = colIdxPrm;
        }
        var chkAll = $(
            $("#" + tableId + " thead tr th:nth-child(" + colIdx + ")").find(
                'input[type="checkbox"]'
            )[0]
        );
        var isCheckAll = chkAll.prop("checked");

        var chkRows = [];
        var aoData = $("#" + tableId)
            .DataTable()
            .data().context[0].aoData;
        if (aoData && aoData.length > 0) {
            aoData.forEach(function (rowData) {
                var chk = $(rowData.anCells).find('input[type="checkbox"]')[colIdx - 1];
                chkRows.push(chk);
            });

            if (isCheckAll) {
                chkRows.forEach(function (chk) {
                    chk.checked = true;
                });
            }

            chkAll.click(function (e) {
                var isChecked = $(e.target).prop("checked");
                chkRows.forEach(function (chk) {
                    chk.checked = isChecked;
                });
            });
            $(chkRows).click(function (e) {
                var checkedLength = chkRows.filter((e1) => e1.checked).length;
                chkAll[0].checked = checkedLength === chkRows.length;
            });
        }
    },
    fnc_checkRowsLazyload: function (tableId, colIdxPrm) {
        var colIdx = 1;
        if (colIdxPrm) {
            colIdx = colIdxPrm;
        }
        var chkAll = $(
            $("#" + tableId + " thead tr th:nth-child(" + colIdx + ")").find(
                'input[type="checkbox"]'
            )[0]
        );
        chkAll[0].checked = false;
        var chkRows = $("#" + tableId + " tbody tr td:nth-child(" + colIdx + ")").find(
            'input[type="checkbox"]'
        );
        chkAll.click(function (e) {
            var isChecked = $(e.target).prop("checked");
            chkRows.each(function (index) {
                chkRows[index].checked = isChecked;
            });
        });
        chkRows.click(function (e) {
            var checkedLength = chkRows.filter((e1) => chkRows[e1].checked).length;
            chkAll[0].checked = checkedLength === chkRows.length;
        });
    },
    fnc_lazyLoad: function (
        tableId,
        columns,
        pagingUrl,
        loadSearchReq,
        drawCallback,
        initComplete,
        options
    ) {
        var searchReq = {};
        if (loadSearchReq) {
            searchReq = loadSearchReq();
        }
        // reset state for tableId
        CMTBL.stateTable[location.hash] = {
            ...(CMTBL.stateTable[location.hash] ? CMTBL.stateTable[location.hash] : {}),
            [tableId]: {},
        };
        // regist msg code
        if (!$("#" + tableId).attr("initEmptyMsgCd")) {
            $("#" + tableId).attr("initEmptyMsgCd", "EM009");
        }
        if (!$("#" + tableId).attr("searchEmptyMsgCd")) {
            $("#" + tableId).attr("searchEmptyMsgCd", "EM010");
        }

        // init datatable
        CMTBL.table = $("#" + tableId).DataTable({
            responsive: false,
            processing: true,
            serverSide: true,
            paging: true,
            lengthMenu: CMTBL.lengthMenu,
            lengthChange: true,
            pageLength: CMTBL.pageLength,
            order: [],
            stateSave: false,
            dom: CMTBL.dom,
            pagingType: "full_numbers",
            language: {
                url: `/assets/common/i18n/datatable_language_${I18N.currentLang}.json`,
            },
            columns: columns,
            ...(options ? options : {}),
            ajax: function (data, callback, settings) {
                // page request
                var pageRequest = CMTBL.fnc_createPageRequest(data, settings);
                if (!searchReq) {
                    searchReq = {};
                }
                var requestPrm = Object.assign(pageRequest, searchReq);
                $.get(pagingUrl, requestPrm, function (response) {
                    callback({
                        draw: response.draw,
                        recordsTotal: response.total,
                        recordsFiltered: response.total,
                        data: response.content,
                    });
                });
            },
            drawCallback: function (settings) {
                var pagination = $(this)
                    .closest(".dataTables_wrapper")
                    .find(".dataTables_paginate");
                pagination.toggle(this.api().page.info().pages > 1);

                KTMenu.createInstances();

                // handle display for state table
                var isInit = CMTBL.fnc_getInitStateTable(tableId);
                var currentPageSize = settings.oSavedState.length;
                var beforePageSize = CMTBL.fnc_getPageSizeStateTable(tableId);

                // when init and change page size
                if (isInit && currentPageSize !== beforePageSize) {
                    CMTBL.fnc_saveStateTable(tableId, true, currentPageSize);
                    CMTBL.fnc_showEmptyMsg(tableId, settings?.aoData?.length < 1, true);
                } else {
                    CMTBL.fnc_saveStateTable(tableId, false, currentPageSize);
                    CMTBL.fnc_showEmptyMsg(tableId, settings?.aoData?.length < 1, false);
                }

                // draw call back
                drawCallback && drawCallback(settings);

                // tooltip
                CMTBL.fnc_triggerTooltip();
            },
            initComplete: function (settings, json) {
                CMTBL.fnc_saveStateTable(tableId, true, settings.oSavedState.length);
                initComplete && initComplete(settings, json);
            },
        });

        // regist trigger reload
        $("#" + tableId).on("CMTBL_reload", function (e, arg) {
            if (loadSearchReq) {
                searchReq = loadSearchReq();
            }
            $("#" + tableId)
                .DataTable()
                .draw();
        });

        // regist trigger reload current state
        $("#" + tableId).on("CMTBL_reloadCurrent", function (e, arg) {
            if (loadSearchReq) {
                searchReq = loadSearchReq();
            }
            $("#" + tableId)
                .DataTable()
                .draw(false);
        });
    },
    fnc_preLoad: function (
        tableId,
        columns,
        listUrl,
        loadSearchReq,
        drawCallback,
        initComplete,
        options
    ) {
        var searchReq = {};
        if (loadSearchReq) {
            searchReq = loadSearchReq();
        }

        // reset state for tableId
        CMTBL.stateTable[location.hash] = {
            ...(CMTBL.stateTable[location.hash] ? CMTBL.stateTable[location.hash] : {}),
            [tableId]: {},
        };
        // regist msg code
        if (!$("#" + tableId).attr("initEmptyMsgCd")) {
            $("#" + tableId).attr("initEmptyMsgCd", "EM009");
        }
        if (!$("#" + tableId).attr("searchEmptyMsgCd")) {
            $("#" + tableId).attr("searchEmptyMsgCd", "EM010");
        }

        // init datatable
        CMTBL.table = $("#" + tableId).DataTable({
            destroy: true,
            responsive: false,
            processing: true,
            serverSide: false,
            paging: true,
            lengthMenu: CMTBL.lengthMenu,
            lengthChange: true,
            pageLength: CMTBL.pageLength,
            order: [],
            stateSave: false,
            dom: CMTBL.dom,
            language: {
                url: `/assets/common/i18n/datatable_language_${I18N.currentLang}.json`,
            },
            columns: columns,
            ...(options ? options : {}),
            ajax: function (data, callback, settings) {
                if (!searchReq) {
                    searchReq = {};
                }
                $.get(listUrl, searchReq, function (response) {
                    callback({
                        recordsTotal: response.length,
                        data: response,
                    });
                });
            },
            drawCallback: function (settings) {
                var pagination = $(this)
                    .closest(".dataTables_wrapper")
                    .find(".dataTables_paginate");
                pagination.toggle(this.api().page.info().pages > 1);

                KTMenu.createInstances();

                // handle display for state table
                var isInit = CMTBL.fnc_getInitStateTable(tableId);
                var currentPageSize = settings.oSavedState.length;
                var beforePageSize = CMTBL.fnc_getPageSizeStateTable(tableId);

                // when init and change page size
                if (!options?.noSearch && !(isInit && currentPageSize !== beforePageSize)) {
                    CMTBL.fnc_saveStateTable(tableId, false, currentPageSize);
                    CMTBL.fnc_showEmptyMsg(tableId, settings?.aoData?.length < 1, false);
                }

                if (options?.noSearch === true) {
                    CMTBL.fnc_showEmptyMsg(tableId, settings?.aoData?.length < 1, true);
                }

                drawCallback && drawCallback(settings);

                // tooltip
                CMTBL.fnc_triggerTooltip();
            },
            initComplete: function (settings, json) {
                CMTBL.fnc_saveStateTable(tableId, true, settings.oSavedState.length);
                CMTBL.fnc_showEmptyMsg(tableId, settings?.aoData?.length < 1, true);
                initComplete && initComplete(settings, json);
            },
        });

        // regist trigger reload
        $("#" + tableId).on("CMTBL_reload", function (e, arg) {
            if (loadSearchReq) {
                searchReq = loadSearchReq();
            }
            $("#" + tableId)
                .DataTable()
                .ajax.reload();
        });

        // regist trigger reload current state
        $("#" + tableId).on("CMTBL_reloadCurrent", function (e, arg) {
            if (loadSearchReq) {
                searchReq = loadSearchReq();
            }
            $("#" + tableId)
                .DataTable()
                .draw(false);
        });
    },
    fnc_dummyLoad: function (tableId, columns, dummyData, pageLength, drawCallback, initComplete) {
        // reset state for tableId
        CMTBL.stateTable[location.hash] = {
            ...(CMTBL.stateTable[location.hash] ? CMTBL.stateTable[location.hash] : {}),
            [tableId]: {},
        };
        // regist msg code
        if (!$("#" + tableId).attr("initEmptyMsgCd")) {
            $("#" + tableId).attr("initEmptyMsgCd", "EM009");
        }
        if (!$("#" + tableId).attr("searchEmptyMsgCd")) {
            $("#" + tableId).attr("searchEmptyMsgCd", "EM010");
        }

        CMTBL.table = $("#" + tableId).DataTable({
            destroy: true,
            responsive: false,
            processing: true,
            serverSide: false,
            paging: true,
            lengthMenu: CMTBL.lengthMenu,
            lengthChange: true,
            pageLength: pageLength ? pageLength : CMTBL.pageLength,
            order: [],
            stateSave: false,
            dom: CMTBL.dom,
            pagingType: "full_numbers",
            data: dummyData,
            language: {
                url: `/assets/common/i18n/datatable_language_${I18N.currentLang}.json`,
            },
            columns: columns,
            drawCallback: function (settings) {
                var pagination = $(this)
                    .closest(".dataTables_wrapper")
                    .find(".dataTables_paginate");
                pagination.toggle(this.api().page.info().pages > 1);

                KTMenu.createInstances();

                // handle display for state table
                var isInit = CMTBL.fnc_getInitStateTable(tableId);
                var currentPageSize = settings.oSavedState.length;
                var beforePageSize = CMTBL.fnc_getPageSizeStateTable(tableId);

                // when init and change page size
                if (isInit && currentPageSize !== beforePageSize) {
                    CMTBL.fnc_saveStateTable(tableId, true, currentPageSize);
                    CMTBL.fnc_showEmptyMsg(tableId, settings?.aoData?.length < 1, true);
                } else {
                    CMTBL.fnc_saveStateTable(tableId, false, currentPageSize);
                    CMTBL.fnc_showEmptyMsg(tableId, settings?.aoData?.length < 1, false);
                }

                drawCallback && drawCallback(settings);

                // tooltip
                CMTBL.fnc_triggerTooltip();
            },
            initComplete: function (settings, json) {
                CMTBL.fnc_saveStateTable(tableId, true, settings.oSavedState.length);
                initComplete && initComplete(settings, json);
            },
        });
        $("#" + tableId).on("CMTBL_reload", function (e, arg) {
            $("#" + tableId)
                .DataTable()
                .data()
                .rows(dummyData)
                .draw();
            $("#" + tableId)
                .DataTable()
                .clear();
            $("#" + tableId)
                .DataTable()
                .clear()
                .rows.add(dummyData)
                .draw();
            /* $('#'+tableId).DataTable().page(currentPage).draw('page');*/
        });
    },
    fnc_reloadDummyTbl(tableId, dummyData, page) {
        $("#" + tableId)
            .DataTable()
            .data()
            .rows(dummyData)
            .draw();
        $("#" + tableId)
            .DataTable()
            .clear();
        $("#" + tableId)
            .DataTable()
            .clear()
            .rows.add(dummyData)
            .draw();
        $("#" + tableId)
            .DataTable()
            .page(page)
            .draw("page");
    },
    fnc_addRow: function (tableId, rowObj, isAddFirst) {
        var table = $("#" + tableId).DataTable();
        if (isAddFirst) {
            var rowData = table.rows().data().toArray();
            rowData.unshift(rowObj);
            table.clear().rows.add(rowData).draw();
            return;
        }
        table.row.add(rowObj).draw();
    },
    fnc_showEmptyMsg: function (tableId, isEmptyPrm, initPrm) {
        var isEmpty =
            typeof isEmptyPrm !== "undefined"
                ? isEmptyPrm
                : $("#" + tableId)
                      .DataTable()
                      .page.info().recordsTotal < 1;
        if (isEmpty) {
            var isInit =
                typeof initPrm !== "undefined" ? initPrm : CMTBL.fnc_getInitStateTable(tableId);
            var initEmptyMsgCd = $("#" + tableId).attr("initEmptyMsgCd") || "EM009";
            var searchEmptyMsgCd = $("#" + tableId).attr("searchEmptyMsgCd") || "EM010";
            const text =
                isInit === true
                    ? MSG[initEmptyMsgCd][I18N.currentLang]
                    : MSG[searchEmptyMsgCd][I18N.currentLang];
            $("#" + tableId + " .dataTables_empty").html(text);
        }
    },
    fnc_renderMasterValue: function (type, code) {
        var values = CM_masterJsonArray.find((t) => t?.type === type && t?.code === code);
        var koValue = values?.value;
        var myValue = values?.valueMy;
        var enValue = values?.valueEn;
        //var currentValue = I18N.currentLang === "ko" ? koValue : myValue;
        var currentValue;
        switch (I18N.currentLang) {
            case "ko":
                currentValue = koValue;
                break;
            case "my":
                currentValue = myValue;
                break;
            case "en":
                currentValue = enValue;
                break;
            default:
                console.log("Invalid language");
        }
        return (
            '<span class="cell-langs" ko="' +
            koValue +
            '" my="' +
            myValue +
            '" en="' +
            enValue +
            '">' +
            currentValue +
            "</span>"
        );
    },
    fnc_renderLangValue: function (koValue, myValue, enValue) {
        //var currentValue = I18N.currentLang === "ko" ? koValue : myValue;
        var currentValue = "";
        switch (I18N.currentLang) {
            case "ko":
                currentValue = koValue;
                break;
            case "my":
                currentValue = myValue;
                break;
            case "en":
                currentValue = enValue;
                break;
            default:
                console.log("Invalid language");
                break;
        }
        return `<span class="cell-langs" ko="${koValue}" my="${myValue}" en="${enValue}">${currentValue}</span>`;
    },
    fnc_reorderRow: function (rows, totalRecords, selector) {
        Array.from(rows).map((row, i) => {
            let elements = $(row).find(selector);
            if (elements.length > 0) {
                elements[0].innerHTML = totalRecords - i;
            }
        });
    },
    fnc_renderDateWithWeekDay: function (
        date,
        dateFormat = "YYYY-MM-DD",
        timeFormat = "HH:mm:ss",
        haveTime = true
    ) {
        try {
            if (!date) return "";

            const _date = moment(date);

            // Format the date and time
            const dateString = _date.format(dateFormat);
            const timeString = _date.format(timeFormat);

            // Get the day of the week
            const dayOfWeekKo = UTL.daysOfWeek("ko")[_date.day()];
            const dayOfWeekMy = UTL.daysOfWeek("en")[_date.day()];
            const dayOfWeekEN = UTL.daysOfWeek("en")[_date.day()];

            // Return the formatted string
            return `${dateString}(${CMTBL.fnc_renderLangValue(dayOfWeekKo, dayOfWeekMy, dayOfWeekEN)}) ${
                haveTime ? timeString : ""
            }`;
        } catch (error) {
            console.error("fnc_formatDateWithWeekDay", error);
        }
    },
    fnc_renderTooltip: function (data, full, maxWidth) {
        data = data || "";
        var separateLines = data.split(/\r?\n|\r|\n/g);

        if (separateLines.length > 1) {
            data = separateLines.join("<br />");
        }
        let html = "<div class='d-flex justify-content-center fixed-table align-items-center'>";
        html += `<div class='summary' style='max-width: ${maxWidth}px;' data-bs-html="true" data-bs-toggle='tooltip' data-bs-placement='bottom' title='${data}'>${
            separateLines[0]
        }${separateLines.length > 1 ? "..." : ""}</div>`;
        if (full.hasImage) {
            html += CMTBL.imageDom;
        }

        if (full.hasFile) {
            html += CMTBL.documentDom;
        }
        html += "</div>";
        return html;
    },
    fnc_triggerTooltip() {
        KTApp.initBootstrapTooltips();
    },
    fnc_renderImg(fileName, width, height, classes) {
        return `<img src='${CONTEXT_PATH}/store/download/${fileName}' width='${width}' height='${height}' class='${classes}' />`;
    },
    checkBoxDom: function (isChecked) {
        return (
            '<div class="form-check form-check-sm form-check-custom d-inline-flex"><input class="form-check-input" type="checkbox" ' +
            (isChecked ? "checked" : "") +
            "/></div>"
        );
    },
    fnc_dummyReload: function (tableId, data) {
        var table = $("#" + tableId).DataTable();
        table.clear();
        table.rows.add(data).draw();
    },
    fnc_renderGender: function (gender) {
        if (!gender) return "";

        const koValue = LANG_GENDER.ko[gender] || "";
        const myValue = LANG_GENDER.my[gender] || "";
        const enValue = LANG_GENDER.en[gender] || "";
        const currentValue = I18N.currentLang === "ko" ? koValue : I18N.currentLang === "my" ? myValue : enValue;
        return `<span class="cell-langs" ko="${koValue}" my="${myValue}" en="${enValue}">${currentValue}</span>`;
    },
    fnc_renderMemberStatus: function (status) {
        const koValue = LANG_MEMBER_STATUS_EXIT.ko[status];
        const myValue = LANG_MEMBER_STATUS_EXIT.my[status];
        const enValue = LANG_MEMBER_STATUS_EXIT.en[status];
        const currentValue = I18N.currentLang === "ko" ? koValue : I18N.currentLang === "my" ? myValue : enValue;
        return `<span class="cell-langs" ko="${koValue}" my="${myValue}" en="${enValue}">${currentValue}</span>`;
    },
    fnc_renderSelectVisitEmbasyStatus: function (type, code, id, visitTime, hasRegistered, isOff, callbackOnChanged) {
        let html = `<select class='thm-form-control ws-text-dark form-select select-master select-visit-status fs-6' data-control='select2' 
                data-hide-search='true' onchange="${callbackOnChanged}" id="select-visit-status-${id}">`;

        const now = moment();
        const visitDt = moment(visitTime);
        CM_masterJsonArray.filter((t) => t.type === type).forEach((t) => {
            let disabled = false;
            if (code === "COMPLETE_REGISTRATION") {
                // if visit time is before current time, disable all options except 'COMPLETE_REGISTRATION' and 'CANCELED'
                disabled = (t.code !== "CANCELED" && t.code !== "COMPLETE_REGISTRATION") || visitDt < now;
            }

            if (code === "CANCELED") {
                // if visit time is after current time, disable all options except 'CANCELED' and 'COMPLETE_REGISTRATION'
                if (visitDt >= now && !(hasRegistered || isOff)) {
                    disabled = t.code !== "CANCELED" && t.code !== "COMPLETE_REGISTRATION";
                } else {
                    // if visit time is before current time, disable all options except 'CANCELED'
                    disabled = t.code !== "CANCELED";
                }
            }

            if (!['CANCELED', 'COMPLETE_REGISTRATION'].includes(code)) {
                // if current status is not 'CANCELED' or 'COMPLETE_REGISTRATION', disable all options 'CANCELED' and 'COMPLETE_REGISTRATION'
                disabled = ['CANCELED', 'COMPLETE_REGISTRATION'].includes(t.code);
            }

            html += `<option value="${t.code}" ${t.code === code ? "selected" : ""}
                            ko="${t.value}" my="${t.valueMy}" en="${t.valueEn}" ${disabled ? "disabled" : ""}>
                            ${I18N.currentLang === "ko" ? t.value : I18N.currentLang === "my" ? t.valueMy : t.valueEn}
                    </option>`;
        });

        html += "</select>";
        return html;
    },
};
