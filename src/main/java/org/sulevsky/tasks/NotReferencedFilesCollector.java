package org.sulevsky.tasks;

/**
 * Created by volodymyr.sulevskyi on 07/09/16.
 */
public class NotReferencedFilesCollector {
//    Obsolete files are automatically removed from storage by periodical backend task. Obsolescence of file is detected by missing references. All files has assigned references (which type of object and unique id is file related to). Files created long time ago (defined by configuration) with no references assigned are considered as obsolete and therefore are subject to garbage collection.
    //TODO implement with quartz
}
