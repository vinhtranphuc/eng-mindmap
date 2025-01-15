const SIDEBAR = {
    fnc_init: function () {
        // when click menu, show submenu
        document.querySelectorAll('.sidebar ul li').forEach(function (menu) {
            let submenu = menu.querySelector('ul');
            let icon = menu.querySelector('.fa-angle-right');
            if (submenu) {
                if (icon) {
                    icon.style.display = 'inline';
                }
                menu.addEventListener('click', function (e) {
                    e.stopPropagation();
                    if (e.target.tagName.toLowerCase() === 'a') {
                        return;
                    }
                    submenu.classList.toggle('hidden');
                    icon.classList.toggle('fa-angle-down');
                    icon.classList.toggle('fa-angle-right');
                });
            }
        });
        // when click hamburger, show sidebar
        document.querySelector('.hamburger').addEventListener('click', () => {
            SIDEBAR.fnc_toggle();
        });

        // when resize window
        if (window.innerWidth <= 1024) {
            document.querySelector('.sidebar').classList.add('hidden');
        } else {
            document.querySelector('.sidebar').classList.remove('hidden');
        }
        window.addEventListener('resize', function () {
            if (window.innerWidth <= 1024) {
                document.querySelector('.sidebar').classList.add('hidden');
            } else {
                document.querySelector('.sidebar').classList.remove('hidden');
            }
        });
        
        // when click content, hide sidebar
        document.querySelector('.content').addEventListener('click', function () {
            if (window.innerWidth <= 1024 && !document.querySelector('.sidebar').classList.contains('hidden')) {
                document.querySelector('.sidebar').classList.add('hidden');
            }
        });
    },
    fnc_toggle: function () {
        document.querySelector('.sidebar').classList.toggle('hidden');
    }
}