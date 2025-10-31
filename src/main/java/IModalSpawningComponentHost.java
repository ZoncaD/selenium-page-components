public interface IModalSpawningComponentHost<T extends IComponentHost<T>> {
    T performModalSpawningComponentAction();
    ModalComponent<T> openModal();
}
