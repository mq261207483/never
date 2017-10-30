/**
 * base operations,abstract the common query、add、edit、remove、save etc operation
 * usage:
 * <script src="path of the script"></script>
 * <script>
 *   var platform = $.platform({uri:'/workflow/console'});
 * </script>
 */
var defaultOptions = {
	query : 'form-query',
	querySubmit : 'button-query-submit',
	queryHandler : null,
	queryReset : 'button-query-reset',
	resetHandler : null,
	queryParamHandler : null,
	add : 'button-add',
	addHandler : null,
	addPostHandler : null,
	edit : 'button-edit',
	editHandler : null,
	editPreHandler : null,
	editPostHandler : null,
	remove : 'button-remove',
	removeHandler : null,
	idExtractor : null,
	view : 'button-view',
	datagrid : 'datagrid',
	dialog : 'dialog-manage',
	manage : 'form-manage',
	save : 'button-manage-save',
	submitDataHandler : null,
	saveHandler : null,
	cancel : 'button-manage-cancel',
	cancelHandler : null,
	uri : ''
};

function BaseHandler(options) {
	this.options = {};
	$.extend(this.options, defaultOptions, options);
}

BaseHandler.prototype.query = function() {
	var queryParams = $('#' + this.options.query).form('serialize');

	if (this.options.queryParamHandler) {
		queryParams = this.options.queryParamHandler(queryParams);
	}

	$('#' + this.options.datagrid).datagrid('load',queryParams);
};

BaseHandler.prototype.reset = function() {
	$('#' + this.options.query).form('reset');
};

BaseHandler.prototype.add = function() {
	$('#' + this.options.dialog).dialog('open').dialog('setTitle', '新增');
	$('#' + this.options.manage).form('clear');
	$('#' + this.options.manage).form('reset');
};

BaseHandler.prototype.edit = function() {
	var row = $('#' + this.options.datagrid).datagrid('getSelected');
	if (row == null) {
		$.messager.alert('提示信息', '请选择一条数据！');
	} else {
		$('#' + this.options.dialog).dialog('open').dialog('setTitle', '编辑');
		$('#' + this.options.manage).form('clear');
		$('#' + this.options.manage).form('load', row);
	}
	;
};

BaseHandler.prototype.remove = function() {
	
	var rows = $('#' + this.options.datagrid).datagrid("getSelections");
	
	if(!rows || rows.length < 1){
		$.messager.alert("提示信息", "请选择至少一条数据！");
		return false;
	}
	
	var uri = this.options.uri;
	
	var datagrid = this.options.datagrid;
	
	var id = null;
	
	if(this.options.idExtractor){
		id = this.options.idExtractor(rows.length === 1 ? rows[0] : rows);
	}else{
		var idArray = [];
		$.each(rows,function(index,item){
			idArray.push(item.id);
		});
		id = idArray.join(',');
	}
	

	$.messager.confirm('提示信息', '您确认要删除记录吗？', function(r) {
		if (r) {
			$.get(contextPath + uri + '/remove.do',{id:id},function(result){
				if(result.success){
					$.messager.alert("提示信息", result.messages.join('<br>'));
					$('#' + datagrid).datagrid("reload");
				}else{
					$.messager.alert("提示信息", result.errors.join('<br>'));
				}
			});
		}
	});
};

BaseHandler.prototype.save = function(params) {
	
	var submitData = $('#' + this.options.manage).form('serialize');
	
	if(this.options.submitDataHandler){
		submitData = this.options.submitDataHandler(submitData);
	}
	
	var finalData = {__entity_data__:JSON.stringify(submitData)};

	if(params){
		$.extend(finalData,params);
	}
	var dialog = this.options.dialog;
	
	var datagrid = this.options.datagrid;
	
	$.post(contextPath + this.options.uri + '/save.do',finalData,function(result){
		if(result.success){
			$.messager.alert("提示信息", result.messages.join('<br>'));
			$('#' + dialog).dialog('close');
			$('#' + datagrid).datagrid('reload');
		}else{
			$.messager.alert("错误信息", result.errors.join('<br>'));
		}
	});
};

BaseHandler.prototype.cancel = function() {
	$('#' + this.options.dialog).dialog('close');
};

(function($){
	function _init(handler){
		var self = handler;
		var options = handler.options;
		
		$('#' + options.querySubmit).click(function(){
			if(options.queryHandler){
				options.queryHandler(self);
			}else{
				self.query();
			}
		});
		$('#' + options.queryReset).click(function(){
			if(options.resetHandler){
				options.resetHandler(self);
			}else{
				self.reset();
			}
		});
		$('#' + options.add).click(function(){
			if(options.addHandler){
				options.addHandler(self);
			}else{
				self.add();
			}
		});
		$('#' + options.edit).click(function(){
			if(options.editHandler){
				options.editHandler(self);
			}else{
				self.edit();
			}
		});
		$('#' + options.remove).click(function(){
			if(options.removeHandler){
				options.removeHandler(self);
			}else{
				self.remove();
			}
		});
		$('#' + options.save).click(function(){
			if(options.saveHandler){
				options.saveHandler(self);
			}else{
				self.save();
			}
		});
		$('#' + options.cancel).click(function(){
			if(options.cancelHandler){
				options.cancelHandler(self);
			}else{
				self.cancel();
			}
		});
		return self;
	}
	
	$.extend({
		platform : function(options){
			return _init(new BaseHandler(options));
		}
	});
})(jQuery);
