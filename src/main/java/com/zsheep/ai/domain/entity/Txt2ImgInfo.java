package com.zsheep.ai.domain.entity;

import lombok.Data;

import java.util.List;

@Data
public class Txt2ImgInfo {
    private Long seed;
    private Integer steps;
    private Integer width;
    private Integer height;
    private String prompt;
    private List<String> styles;
    private Long subseed;
    private List<Long> all_seeds;
    private Integer cfg_scale;
    private Integer clip_skip;
    private List<String> infotexts;
    private Integer batch_size;
    private List<String> all_prompts;
    private List<Long> all_subseeds;
    private String sampler_name;
    private String job_timestamp;
    private Double restore_faces;
    private String sd_model_hash;
    private String negative_prompt;
    private Integer subseed_strength;
    private Double denoising_strength;
    private Integer seed_resize_from_h;
    private Integer seed_resize_from_w;
    private List<String> all_negative_prompts;
    private Integer index_of_first_image;
    private String face_restoration_model;
    private Boolean is_using_inpaIntegering_conditioning;
}
