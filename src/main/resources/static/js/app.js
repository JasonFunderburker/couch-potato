var TorrentItem = React.createClass({
    getInitialState: function () {
        return {
            display: true,
            torrent: this.props.torrent,
            rowStyle: Row.getStyle(this.props.torrent),
            checking: false
        };
    },
    handleDelete() {
        var self = this;
        $.ajax({
            url: 'http://localhost:4991/couch-potato/itemList/' + self.state.torrent.id,
            type: 'DELETE',
            success: function (result) {
                self.setState({display: false});
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(xhr.responseJSON.message);
            }
        });
    },
    handleCheck() {
        var self = this;
        self.setState({checking: true});
        $.ajax({
            url: 'http://localhost:4991/couch-potato/itemList/' + self.state.torrent.id + '/check',
            type: 'POST',
            success: function (updatedTorrent) {
                self.updateTorrent(updatedTorrent);
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(xhr.responseJSON.message);
            }
        });
    },
    updateTorrent: function(updatedTorrent) {
        this.setState({
            torrent: updatedTorrent,
            rowStyle: Row.getStyle(updatedTorrent),
            checking: false
        })
    },
    render: function () {
        let torrent = this.state.torrent;
        if (this.state.display === false) return null;
        else return (
            <tr className={this.state.rowStyle}>
                <td>
                    <div className="btn-group-vertical btn-group-lg">
                        <button className="btn btn-danger" onClick={this.handleDelete}>Delete</button>
                        <button className="btn btn-primary" onClick={this.handleCheck}>Check</button>
                    </div>
                </td>
                <td><a href={torrent.link}>{torrent.name}</a></td>
                <td>{this.state.checking ? <div className="loader"></div> : torrent.status}</td>
                <td>{torrent.updateDate}</td>
                <td>{torrent.errorText}</td>
            </tr>
        );
    }
});

class Row {
    static getStyle(torrent) {
        let rowStyleValue;
        switch (torrent.status) {
            case "NEW":
                rowStyleValue = 'info';
                break;
            case "UNCHANGED":
                rowStyleValue = 'warning';
                break;
            case "ERROR":
            case "DOWNLOAD_ERROR":
                rowStyleValue = 'danger';
                break;
            case "DOWNLOADED":
                rowStyleValue = 'success';
                break;
            default:
                rowStyleValue = '';
        }
        return rowStyleValue;
    }
}

var TorrentsTable = React.createClass({
    render: function () {
        var rows = [];
        this.props.torrents.forEach(function (torrent) {
            rows.push(<TorrentItem torrent={torrent} key={torrent.id}/>)
        });
        return (
            <table className="table table-striped">
                <thead>
                <tr>
                    <th className="col-md-1">Actions</th>
                    <th className="col-md-5">Name</th>
                    <th>Status</th>
                    <th>Last Update</th>
                    <th>Error Text</th>
                </tr>
                </thead>
                <tbody>{rows}</tbody>
            </table>
        );
    }
});
var TorrentForm = React.createClass({
    getInitialState: function () {
        return {
            inputValue: ''
        };
    },
    handleAdd() {
        var self = this;
        $.ajax({
            url: 'http://localhost:4991/couch-potato/itemList',
            type: 'POST',
            contentType: 'application/json',
            data: '{"link": "' + self.state.inputValue + '"}',
            success: function (result) {
                alert("Success added");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(xhr.responseJSON.message);
            }
        });
    },
    updateInputValue: function (evt) {
        this.setState({
            inputValue: evt.target.value
        });
    },
    render: function () {
        return (
            <form>
                <div className="form-group">
                    <label for="link">Enter torrent source link</label>
                    <input type="link" className="form-control" id="link" value={this.state.inputValue}
                           onChange={this.updateInputValue}/>
                </div>
                <button type="submit" className="btn btn-success" onClick={this.handleAdd}>Add</button>
            </form>
        );
    }
});

var AddModal = React.createClass({
    render: function () {
        return (
            <div id="myModal" className="modal fade" role="dialog">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <button type="button" className="close" data-dismiss="modal">&times;</button>
                            <h4 className="modal-title">Add torrent item</h4>
                        </div>
                        <div className="modal-body">
                            <TorrentForm/>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});
var AddButton = React.createClass({
    render: function () {
        return <button type="button" className="btn btn-lg btn-success" data-toggle="modal" data-target="#myModal">Add</button>
    }
});
var CheckButton = React.createClass({
    handleCheckAll() {
        var self = this;
        $.ajax({
            url: 'http://localhost:4991/couch-potato/itemList/checkAll',
            type: 'POST',
            success: function (result) {
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(xhr.responseJSON.message);
            }
        });
    },
    render: function () {
        return <button type="button" className="btn btn-lg btn-primary" onClick={this.handleCheckAll}>Check Now</button>;
    }
});

var App = React.createClass({

    loadTorrentItems: function () {
        var self = this;
        $.ajax({
            url: "http://localhost:4991/couch-potato/itemList"
        }).then(function (data) {
            self.setState({torrents: data})
        });
    },

    getInitialState: function () {
        return {torrents: []};
    },

    componentDidMount: function () {
        this.loadTorrentItems();
    },
    render() {
        return (
            <div>
                <div className="form-inline">
                    <AddButton/> <CheckButton/>
                </div>
                <AddModal/>
                <TorrentsTable torrents={this.state.torrents}/>
            </div>);
    }
});

ReactDOM.render(
    <App/>, document.getElementById('root')
);