/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mcpgateway.workbench;
import org.springframework.stereotype.Service;
import java.math.*;
import java.util.*;
import static cn.zhuatech.mcpgateway.workbench.WorkbenchInput.*;
/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class AiWorkbenchService {
    /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
    public Map<String,Object> execute(Map<String,Object> r){
        validate(r);

        keys(r,"schema","arguments");var fields=rows(r,"schema",1,50);var args=object(r.get("arguments"));if(args.size()>50)throw new IllegalArgumentException("参数最多50项");Set<String> known=new HashSet<>();List<String> violations=new ArrayList<>();
        for(var field:fields){keys(field,"name","type","required","min","max","maxLength");String name=id(field,"name"),type=text(field,"type",16);if(!known.add(name))throw new IllegalArgumentException("字段声明重复");boolean required=flag(field,"required");if(!Set.of("string","integer","number","boolean").contains(type))throw new IllegalArgumentException("不支持的字段类型");
            if(type.equals("string")&&field.containsKey("maxLength"))integer(field,"maxLength",1,8000);
            BigDecimal min=field.containsKey("min")?decimal(field,"min",-1e12,1e12):BigDecimal.valueOf(-1e12),max=field.containsKey("max")?decimal(field,"max",-1e12,1e12):BigDecimal.valueOf(1e12);if(min.compareTo(max)>0)throw new IllegalArgumentException("字段范围倒置");
            Object value=args.get(name);if(value==null){if(required)violations.add(name+":REQUIRED");continue;}
            boolean valid=switch(type){case "string"->value instanceof String s&&s.length()<=(field.containsKey("maxLength")?integer(field,"maxLength",1,8000):8000);case "boolean"->value instanceof Boolean;case "integer","number"->{try{BigDecimal d=number(args,name,min,max);yield !type.equals("integer")||d.stripTrailingZeros().scale()<=0;}catch(IllegalArgumentException e){yield false;}}default->false;};
            if(!valid)violations.add(name+":TYPE_OR_RANGE");
        }
        args.keySet().stream().filter(x->!known.contains(x)).sorted().forEach(x->violations.add(x+":UNKNOWN"));
        return Map.of("valid",violations.isEmpty(),"violations",List.copyOf(violations),"executesTool",false,"authorization","CALLER_DECLARATION_CHECK_ONLY");

    }
}
