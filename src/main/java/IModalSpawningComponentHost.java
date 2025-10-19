public interface IModalSpawningComponentHost<T extends IComponentHost<T>> extends IComponentHost<T> {
    ModalSpawningComponent<T> getModalSpawningComponent();

    default T performModalSpawningComponentAction() {
        getModalSpawningComponent().performModalSpawningComponentAction();
        return getHost();
    }

    default ModalComponent<T> openModal() {
        return getModalSpawningComponent().openModal();
    }
}
