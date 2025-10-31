public interface IParentComponentHost<T extends IComponentHost<T>> extends ISimpleComponentHost<ParentComponent<T>> {
    T performParentComponentAction();
    boolean isParentComponentSomething();
}
