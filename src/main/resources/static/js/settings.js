var TorrentAccount = React.createClass({
    getInitialState: function () {
        let login = this.props.account.username ? this.props.account.username : "";
        let password = this.props.account.hash ? this.props.account.hash : "";
        let alreadyFilled = false;
        if (login) alreadyFilled = true;

        return {
            login: login,
            password: password,
            alreadyFilled: alreadyFilled
        };
    },
    updateLoginValue: function (evt) {
        this.setState({
            login: evt.target.value
        });
    },
    updatePasswordValue: function (evt) {
        this.setState({
            password: evt.target.value
        });
    },
    handleSave() {
        let self = this;
        $.ajax({
            url: 'http://localhost:4991/couch-potato/torrentTypes/' + self.props.account.id + '/account',
            type: 'POST',
            contentType: 'application/json',
            data: '{"username": "' + self.state.login + '",' +
            '"hash":"' + self.state.password + '"}',
            success: function (result) {
                self.setState({
                    alreadyFilled: true
                });
                alert("Success saved");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(xhr.responseJSON.message);
            }
        });
    },
    handleChange() {
        this.setState({
            login: "",
            password: "",
            alreadyFilled: false
        });
    },
    render: function () {
        let typeName = this.props.account.type;
        return (
            <tr>
                <td>
                    <h2>{typeName}</h2>
                    <div className="form">
                        <div className="form-group">
                            <label for="login">Enter account "{typeName}" login</label>
                            <input type="login" className="form-control" id="login"
                                   value={this.state.login} onChange={this.updateLoginValue} readOnly={this.state.alreadyFilled}/>
                        </div>
                        <div className="form-group">
                            <label for="password">Enter account "{typeName}" password</label>
                            <input type="password" className="form-control" id="password"
                                   value={this.state.password} onChange={this.updatePasswordValue} readOnly={this.state.alreadyFilled}/>

                        </div>
                        <button className="btn btn-primary" onClick={this.handleSave} disabled={this.state.alreadyFilled}>Save</button>
                        <button className="btn btn-success" onClick={this.handleChange} disabled={!this.state.alreadyFilled}>Change</button>
                    </div>
                </td>
            </tr>
        );
    }
});

var ScheduleSettings = React.createClass({
    getInitialState: function () {
        return {scheduleTime: ""};
    },
    updateScheduleTime: function (evt) {
        this.setState({
            scheduleTime: evt.target.value
        });
    },
    handleSetScheduleTime() {
        let self = this;
        $.ajax({
            url: 'http://localhost:4991/couch-potato/itemList/scheduleCheck',
            type: 'POST',
            contentType: 'application/json',
            data: '{"scheduleTime": "' + self.state.scheduleTime + '"}',
            success: function (result) {
                alert("Success saved");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(xhr.responseJSON.message);
            }
        });
    },
    componentDidMount: function () {
        let self = this;
        $.ajax({
            url: "http://localhost:4991/couch-potato/itemList/scheduleCheck"
        }).then(function (data) {
            if (data !== null) {
                self.setState({scheduleTime: data.scheduleTime})
            }
        });
    },
    render: function () {
        return (
            <div className="panel panel-info">
                <div className="panel-heading">Check settings</div>
                <div className="panel-body">
                    <div className="form-inline">
                        <div className="form-group">
                            <label for="scheduleTime">Schedule check torrents list every day at : </label>
                            <input type="time" className="form-control" id="scheduleTime"
                                   value={this.state.scheduleTime}
                                   onChange={this.updateScheduleTime}/>
                        </div>
                        <button className="btn btn-primary" onClick={this.handleSetScheduleTime}>Save
                        </button>
                    </div>
                </div>
            </div>
        );
    }
});

var RssSettings = React.createClass({
    getInitialState: function () {
        return {rssUrl: ""};
    },
    handleFocus: function(event) {
        event.target.select();
    },
    handleGenerate() {
        let self = this;
        $.ajax({
            url: 'http://localhost:4991/couch-potato/rss/public/generate',
            type: 'POST',
            contentType: 'application/json',
            success: function (data) {
                self.setState({rssUrl: data.rssUrl})
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(xhr.responseJSON.message);
            }
        });
    },
    handleInvalidate() {
        let self = this;
        $.ajax({
            url: 'http://localhost:4991/couch-potato/rss/public/invalidate',
            type: 'POST',
            contentType: 'application/json',
            success: function (data) {
                self.setState({rssUrl: ""})
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(xhr.responseJSON.message);
            }
        });
    },
    componentDidMount: function () {
        let self = this;
        $.ajax({
            url: "http://localhost:4991/couch-potato/rss/public"
        }).then(function (data) {
            if (data !== null && data.rssUrl !== null) {
                self.setState({rssUrl: data.rssUrl})
            } else {
                self.setState({rssUrl: ""})
            }
        });
    },
    render: function () {
        return (
            <div className="panel panel-info">
                <div className="panel-heading">RSS settings</div>
                <div className="panel-body">
                    <div className="form">
                        <div className="form-group">
                            <label for="publicRssUrl">Public RSS URL</label>
                            <div className="panel panel-warning">
                                <div className="panel-heading">Warning!Everyone who got this link has access to rss page</div>
                            </div>
                            <input readOnly="readOnly" className="form-control" id="publicRssUrl"
                                   value={this.state.rssUrl} onClick={this.handleFocus}/>
                        </div>
                        <button className="btn btn-primary" onClick={this.handleGenerate}>Generate New</button>
                        <button className="btn btn-danger" onClick={this.handleInvalidate}>Invalidate</button>
                        <a className="btn btn-warning right" href="http://localhost:4991/couch-potato/rss">RSS</a>
                    </div>
                </div>
            </div>
        );
    }
});

var TorrentAccountsTable = React.createClass({
    render: function () {
        let rows = [];
        this.props.accounts.forEach(function (account) {
            rows.push(<TorrentAccount account={account} key={account.id}/>)
        });
        return (
            <div className="panel panel-info">
                <div className="panel-heading">Accounts settings</div>
                <div className="panel-body">
                    <table className="table table-striped">
                        <tbody>{rows}</tbody>
                    </table>
                </div>
            </div>
        );
    }
});

var Settings = React.createClass({

    loadTorrentAccounts: function () {
        var self = this;
        $.ajax({
            url: "http://localhost:4991/couch-potato/torrentTypes"
        }).then(function (data) {
            self.setState({accounts: data})
        });
    },

    getInitialState: function () {
        return {accounts: []};
    },

    componentDidMount: function () {
        this.loadTorrentAccounts();
    },
    render() {
        return (
            <div className="panel-group">
                <ScheduleSettings />
                <RssSettings />
                <TorrentAccountsTable accounts={this.state.accounts}/>
            </div>);
    }
});

ReactDOM.render(
    <Settings />, document.getElementById('settings')
);
