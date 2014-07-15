package pl.edu.icm.saos.importer.commoncourt.court;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import com.google.common.collect.Lists;


/**
 * @author Łukasz Dumiszewski
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "court")
public class XmlCommonCourt {

    @XmlAttribute(name="id", required=true)
    private String id;
    
    @XmlElement(name="model")
    private CourtData data = new CourtData();

    
    //------------------------ GETTERS --------------------------
    
    public String getId() {
        return id;
    }


    public String getName() {
        return data.getName();
    }
    
    
    public List<Department> getDepartments() {
        return data.getDepartments();
    }


    //------------------------ LOGIC --------------------------
    
    @Override
    public String toString() {
        return "XmlCommonCourt [id=" + id + ", name=" + getName() + ", departments=" + getDepartments() + "]";
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public CourtData getData() {
        return data;
    }


    public void setId(String id) {
        this.id = id;
    }


    public void setName(String name) {
        this.data.setName(name);
    }
    
    public void setDepartments(List<Department> departments) {
        this.data.setDepartments(departments);
    }
    
    
    //-----------------------------------------------------------------
    //------------------------ Inner classes --------------------------
    //-----------------------------------------------------------------
    
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CourtData {

        @XmlElement
        private String name;
        
        @XmlElementWrapper(name="departments")
        @XmlElement(name="department")
        private List<Department> departments = Lists.newArrayList();
        

        //------------------------ GETTERS --------------------------
        
        public String getName() {
            return name;
        }

        public List<Department> getDepartments() {
            return departments;
        }
        
        
        //------------------------ LOGIC --------------------------
        
        
        
        //------------------------ SETTERS --------------------------
        
        public void setName(String name) {
            this.name = name;
        }

        public void setDepartments(List<Department> departments) {
            this.departments = departments;
        }

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Department {
       
        
        @XmlAttribute
        private String id;

        @XmlValue
        private String name;
        
        
        public Department() {
            super();
        }

        public Department(String id, String name) {
            super();
            this.id = id;
            this.name = name;
        }
        
        //------------------------ GETTERS --------------------------
        
        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        
        //------------------------ LOGIC --------------------------
        
        @Override
        public String toString() {
            return "Department [id=" + id + ", name=" + name + "]";
        }
        
        
        //------------------------ SETTERS --------------------------

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

       
    }




}
