export default class BaseStore {
    constructor(initialData) {
        this.state = initialData;
        this.watchers = {};
    }

    value(property) {
        return this.state[property];
    }

    get(property) {
        return this.state[property];
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
            callback(this.state[property]);
        });
    }

    setState(state) {
        this.state = _.extend({}, this.state, state);

        _.each(state, (value, key) => this.notifyUpdateTo(key));
    }
}
