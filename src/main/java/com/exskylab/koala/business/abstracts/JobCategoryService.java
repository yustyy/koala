package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.entities.JobCategory;

import java.util.UUID;

public interface JobCategoryService {
    JobCategory getJobCategoryById(UUID categoryId);
}
