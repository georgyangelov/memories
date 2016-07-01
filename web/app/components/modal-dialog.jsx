export default class ModalDialog extends React.Component {
    componentDidUpdate() {
        if (this.props.visible) {
            document.body.style.overflow = 'hidden';
        } else {
            document.body.style.overflow = '';
        }
    }

    componentWillUnmount() {
        document.body.style.overflow = '';
    }

    close() {
        appHistory.push(this.props.returnTo);
    }

    render() {
        let children = React.Children.map(this.props.children, (child) => {
            return React.cloneElement(child, {close: this.close.bind(this)})
        });

        return <div className={cx('modal-dialog-container', {visible: this.props.visible})}>
            <div className="dialog-overlay" onClick={this.close.bind(this)}></div>
            <div className="dialog-body">{children}</div>
        </div>;
    }
}
