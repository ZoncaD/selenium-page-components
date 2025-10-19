public interface IParentComponentHost<T extends IComponentHost<T>> extends IComponentHost<T>, ISimpleComponentHost<T> {
    ParentComponent getParentComponent();

    @Override
    default SimpleComponent getSimpleComponent() {
        return getParentComponent().getSimpleComponent();
    }

    default T performParentComponentAction() {
        getParentComponent().performParentComponentAction();
        return getHost();
    }

    default boolean isParentComponentSomething() {
        return getParentComponent().isParentComponentSomething();
    }
}
