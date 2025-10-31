public interface ISimpleComponentHost<T extends IComponentHost<T>> extends IComponentHost<T> {
    T performSimpleComponentAction();
    String getSimpleComponentValue();
}
