public class Main {
    public static void main(String[] args) {
        SomePage page = new SomePage();

        int ignoredNum = page.performSomePageAction().getSomePageNumber(5);
        page.performSomePageAction().openModal();
        page.performSomePageAction().performModalSpawningComponentAction();

        ModalComponent<SomePage> modal = page.performSomePageAction().openModal();
        modal = page.performSomePageAction().openModal().performModalAction();
        String value = modal.performSimpleComponentAction().getSimpleComponentValue();

        page = modal.performModalAction().close();
        page = page.performParentComponentAction().performSimpleComponentAction().performModalSpawningComponentAction();
        boolean isParent = page.isParentComponentSomething();
    }
}
