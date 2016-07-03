export default class StoreAwareComponent extends React.Component {
    constructor(props, storeProps) {
        super(props);

        storeProps = storeProps || [];
        this.state = this.state || {};

        this._storeWatchers = [];

        _.each(storeProps, (storeAndProp, propName) => {
            // TODO: Support deeply nested properties
            let [store, prop] = storeAndProp.split('.');
            let watcher = global[store].watch(prop, (value) => {
                this.setState({
                    [propName]: value
                });
            });

            this.state[propName] = global[store].value(prop);

            this._storeWatchers.push(watcher);
        });
    }

    componentWillUnmount() {
        this._storeWatchers.forEach((unwatch) => unwatch());
    }
}
