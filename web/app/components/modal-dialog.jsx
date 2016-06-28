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

    hide() {
        history.push(this.props.returnTo);
    }

    render() {
        return <div className={cx('modal-dialog-container', {visible: this.props.visible})}>
            <div className="dialog-overlay" onClick={this.hide.bind(this)}></div>
            <div className="dialog-body">{this.props.children}</div>
        </div>;
    }
}
