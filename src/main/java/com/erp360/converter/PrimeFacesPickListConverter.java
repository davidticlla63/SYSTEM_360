/**
 * @author WILSON
 */
package com.erp360.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

import com.erp360.model.Usuario;



/**
 * @author WILSON
 *
 */
@FacesConverter(value = "primeFacesPickListConverter")
public class PrimeFacesPickListConverter implements Converter {
@Override
public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
    Object ret = null;
    if (arg1 instanceof PickList) {
        Object dualList = ((PickList) arg1).getValue();
        DualListModel dl = (DualListModel) dualList;
        for (Object o : dl.getSource()) {
            String id = "" + ((Usuario) o).getId();
            if (arg2.equals(id)) {
                ret = o;
                break;
            }
        }
        if (ret == null)
            for (Object o : dl.getTarget()) {
                String id = "" + ((Usuario) o).getId();
                if (arg2.equals(id)) {
                    ret = o;
                    break;
                }
            }
    }
    return ret;
}

@Override
public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
    String str = "";
    if (arg2 instanceof Usuario) {
        str = "" + ((Usuario) arg2).getId();
    }
    return str;
}
}