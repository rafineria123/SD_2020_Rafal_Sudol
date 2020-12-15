function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}
document.getElementById ("kategorie-button").addEventListener ("click", collapseSklepy, false);
document.getElementById ("sklepy-button").addEventListener ("click", collapseKategorie, false);
document.getElementById ("navbar-toggler-button").addEventListener ("click", collapseNavbar, false);


async function collapseKategorie() {
    $('#kategorie').collapse('hide');
    $('#navbarTogglerDemo02').collapse('hide');
    await sleep(350);
    $('#sklepy').collapse('toggle');
}
async function collapseSklepy() {
    $('#sklepy').collapse('hide');
    $('#navbarTogglerDemo02').collapse('hide');
    await sleep(350);
    $('#kategorie').collapse('toggle');

}
async function collapseNavbar() {
    $('#sklepy').collapse('hide');
    $('#kategorie').collapse('hide');
    await sleep(350);
    $('#navbarTogglerDemo02').collapse('toggle');
}
