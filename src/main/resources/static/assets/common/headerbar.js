const HEADERBAR = {
    fnc_init: function () {
        // when click accountDropdown, show dropdownContent
        document.getElementById('accountDropdown').addEventListener('click', function() {
            document.getElementById('dropdownContent').classList.toggle('show');
        });
        window.addEventListener('click', function(e) {
            if (!e.target.matches('#accountDropdown') && !e.target.closest('.dropdown-content')) {
                document.getElementById('dropdownContent').classList.remove('show');
            }
        });
    },
}