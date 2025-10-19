public interface ISimpleComponentHost<T extends IComponentHost<T>> extends IComponentHost<T> {
    SimpleComponent getSimpleComponent();

    default T performSimpleComponentAction() {
        getSimpleComponent().performSimpleComponentAction();
        return getHost();
    }

    default String getSimpleComponentValue() {
        return getSimpleComponent().getSimpleComponentValue();
    }
}
