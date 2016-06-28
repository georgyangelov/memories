export default class App extends React.Component {
    componentWillReceiveProps(newProps) {
        var routeWillChange = newProps.location.key !== this.props.location.key &&
                              newProps.location.state &&
                              newProps.location.state.modal;

        if (routeWillChange) {
            this.previousChildren = this.props.children;
        }
    }

    render() {
        var { location } = this.props;

        var isModal = location.state &&
                      location.state.modal &&
                      this.previousChildren;

        return <views.Layout location={this.props.location}>
            {isModal ? this.previousChildren : this.props.children}

            <ModalDialog visible={isModal} returnTo={isModal && location.state.returnTo}>
                {isModal && this.props.children}
            </ModalDialog>
        </views.Layout>;
    }
}
