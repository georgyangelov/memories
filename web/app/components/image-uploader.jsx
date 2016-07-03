export default class ImageUploader extends React.Component {
    constructor(...args) {
        super(...args);

        this.state = {
            errors: []
        };
    }

    render() {
        return this.state.file ? this.renderWithFile() : this.renderWithoutFile();
    }

    renderWithFile() {
        return <form className="image-uploader" onSubmit={this.upload.bind(this)}>
            <div className="metadata-panel">
                <input type="text" className="form-control" placeholder="Image name"
                       value={this.state.name}
                       onChange={(e) => this.setState({name: e.target.value})} />
                <input type="text" className="form-control" placeholder="Tags"
                       value={this.state.tags}
                       onChange={(e) => this.setState({tags: e.target.value})} />

                {this.state.errors.map((error) => <div className="error">{error}</div>)}
            </div>

            <Dropzone className="preview" onDrop={this.onDrop.bind(this)} multiple={false}>
                <img src={this.state.file.preview} />
            </Dropzone>

            <div className="upload-panel">
                <button type="submit" className="btn btn-primary btn-lg">Upload</button>
                <button type="button" onClick={this.reset.bind(this)} className="btn btn-lg">Cancel</button>
            </div>
        </form>;
    }

    renderWithoutFile() {
        return <div className="image-uploader">
            <Dropzone className="placeholder" onDrop={this.onDrop.bind(this)} multiple={false}>
                Drop a picture here or click to upload.
            </Dropzone>
        </div>;
    }

    onDrop(files) {
        this.setState({
            file: files[0],
            errors: [],
            name: '',
            tags: ''
        });
    }

    reset() {
        this.setState({file: null});
    }

    upload(event) {
        event.preventDefault();

        Image.create(this.state.file, this.state.name, this.state.tags, (error, data) => {
            if (error) {
                this.setState({errors: error.messages || ['Internal server error']});
                return;
            }

            this.reset();
            ImageStore.reload();
        });
    }
}
