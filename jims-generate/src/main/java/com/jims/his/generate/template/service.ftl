/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package ${packageName}.${moduleName}.service${subModuleName};

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jims.his.common.service.BaseService;
import com.jims.his.common.config.Global;
import com.jims.his.common.persistence.Page;
import com.jims.his.common.web.BaseController;
import com.jims.his.common.utils.StringUtils;
import com.jims.his.modules.sys.entity.User;
import com.jims.his.modules.sys.utils.UserUtils;
import ${packageName}.${moduleName}.entity${subModuleName}.${ClassName};
import ${packageName}.${moduleName}.dao${subModuleName}.${ClassName}Dao;

/**
 * ${functionName}Service
 * @author ${classAuthor}
 * @version ${classVersion}
 */
@Component
@Transactional(readOnly = true)
public class ${ClassName}Service extends BaseService {

	@Autowired
	private ${ClassName}Dao ${className}Dao;
	
	public ${ClassName} get(String id) {
		return ${className}Dao.get(id);
	}
	
	public Page<${ClassName}> find(Page<${ClassName}> page, ${ClassName} ${className}) {
		DetachedCriteria dc = ${className}Dao.createDetachedCriteria();
		//if (StringUtils.isNotEmpty(${className}.getName())){
		//	dc.add(Restrictions.like("name", "%"+${className}.getName()+"%"));
		//}
		//dc.add(Restrictions.eq(${ClassName}.FIELD_DEL_FLAG, ${ClassName}.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return ${className}Dao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(${ClassName} ${className}) {
		${className}Dao.save(${className});
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		${className}Dao.deleteById(id);
	}
	
}
