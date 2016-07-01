export default class BaseStore {
    constructor(initialData) {
        this.data = initialData;
        this.watchers = {};
    }

    value(property) {
        return this.data[property];
    }

    watch(property, callback) {
        this.watchers[property] = this.watchers[property] || [];
        this.watchers[property].push(callback);

        return () => this.unwatch(property, callback);
    }

    unwatch(property, callback) {
        let watchers = this.watchers[property];

        watchers.splice(watchers.indexOf(callback), 1);
    }

    notifyUpdateTo(property) {
        (this.watchers[property] || []).forEach((callback) => {
            callback(this.data[property]);
        });
    }

    setState(data) {
        this.data = _.merge({}, this.data, data);

        _.each(data, (value, key) => this.notifyUpdateTo(key));
    }
}
