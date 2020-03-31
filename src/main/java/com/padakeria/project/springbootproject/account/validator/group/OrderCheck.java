package com.padakeria.project.springbootproject.account.validator.group;

import javax.validation.GroupSequence;

@GroupSequence(value = {NotBlankGroup.class, LengthCheckGroup.class, PatternGroup.class})
public interface OrderCheck {
}
