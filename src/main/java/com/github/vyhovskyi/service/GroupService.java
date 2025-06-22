package com.github.vyhovskyi.service;

import com.github.vyhovskyi.dao.GroupDao;
import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public class GroupService {
    private final GroupDao groupDao;

    public GroupService(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public List<Group> getAllGroups() {
        try{
            return groupDao.getAllGroups();
        }catch (Exception e) {
            throw new ServiceException("Falied to fetch all groups", e);
        }
    }

    public Optional<Group> getGroupById(String name) {
        try{
            return groupDao.getGroupByName(name);
        }catch (Exception e){
            throw new ServiceException("Failed to fetch group by name", e);
        }
    }

    public void addGroup(Group group) {
        try {
            groupDao.createGroup(group);
        }catch (Exception e){
            throw new ServiceException("Failed to add group", e);
        }
    }

    public void updateGroup(Group group, String oldName) {
        try {
            groupDao.updateGroup(group, oldName);
        }catch (Exception e){
            throw new ServiceException("Failed to update group", e);
        }
    }

    public void deleteGroup(String name) {
        try {
            groupDao.deleteGroup(name);
        }catch (Exception e){
            throw new ServiceException("Failed to delete group", e);
        }
    }

    public List<Group> getGroupsByFilter(String groupName, String groupDescription, int limit, int offset){
        try{
            return groupDao.getGroupsByFilter(groupName, groupDescription, limit, offset);
        }catch (Exception e){
            throw new ServiceException("Failed to fetch groups by filter", e);
        }
    }

    public List<Group> getGroupsByFilter(String groupName, String groupDescription){
        try{
            return groupDao.getGroupsByFilter(groupName, groupDescription);
        }catch (Exception e){
            throw new ServiceException("Failed to fetch groups by simplified filter", e);
        }
    }

}
