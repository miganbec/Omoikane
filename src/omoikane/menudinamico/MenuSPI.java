/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.menudinamico;

import java.util.ArrayList;
import java.util.ServiceLoader;
import com.phesus.omoikaneapi.Menus.MenuBean;

/**
 *
 * @author Octavio
 */
public class MenuSPI {
    ServiceLoader<MenuBean> sl;

    public MenuSPI() {
        sl = ServiceLoader.load(MenuBean.class);
        
        for (MenuBean menuBean : sl) {
            System.out.println("......."+menuBean.getDisplayName());
        }
        System.out.println("----"+sl.toString());
    }
    public ServiceLoader<MenuBean> getModulos() {
        return sl;
    }

}
