/*
 * Copyright (C) 2015 Mantas Varnagiris.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.mvcoding.financius.feature.tag

import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.Observable
import rx.subjects.PublishSubject

class TagsPresenterTest {
    val tagSelectedSubject = PublishSubject.create<Tag>()
    val tagCreateSubject = PublishSubject.create<Unit>()
    val saveSubject = PublishSubject.create<Unit>()
    val archiveSubject = PublishSubject.create<Tag>()
    val tagsCache = mock(TagsCache::class.java)
    val view = mock(TagsPresenter.View::class.java)

    @Before
    fun setUp() {
        given(view.onTagSelected()).willReturn(tagSelectedSubject)
        given(view.onCreateTag()).willReturn(tagCreateSubject)
        given(view.onSave()).willReturn(saveSubject)
        given(view.onArchiveTag()).willReturn(archiveSubject)
        given(tagsCache.tags()).willReturn(Observable.empty())
    }

    @Test
    fun setsDisplayType() {
        val presenter = presenterWithDisplayTypeView()

        presenter.onAttachView(view)

        verify(view).setDisplayType(TagsPresenter.DisplayType.VIEW)
    }

    @Test
    fun doesNotSetSelectedTagsWhenDisplayTypeIsView() {
        val presenter = presenterWithDisplayTypeView()

        presenter.onAttachView(view)

        verify(view, never()).setSelectedTags(anySetOf(Tag::class.java))
    }

    @Test
    fun setsSelectedTagsWhenDisplayTypeIsMultiChoice() {
        val selectedTags = setOf(aTag(), aTag())
        val presenter = TagsPresenter(tagsCache, TagsPresenter.DisplayType.MULTI_CHOICE, selectedTags)
        presenter.onAttachView(view)

        verify(view).setSelectedTags(selectedTags)
    }

    @Test
    fun showsSelectedTagWhenTagIsSelectedAndDisplayTypeIsMultiChoice() {
        val presenter = presenterWithDisplayTypeMultiChoice()
        val tag = aTag()
        presenter.onAttachView(view)

        selectTag(tag)

        verify(view).setTagSelected(tag, true)
    }

    @Test
    fun showsDeselectedTagWhenTagIsDeselectedAndDisplayTypeIsMultiChoice() {
        val presenter = presenterWithDisplayTypeMultiChoice()
        val tag = aTag()
        presenter.onAttachView(view)

        selectTag(tag)
        selectTag(tag)

        verify(view).setTagSelected(tag, false)
    }

    @Test
    fun showsTagsFromTagsRepository() {
        val presenter = presenterWithDisplayTypeView()
        val tags = listOf(aTag(), aTag(), aTag())
        given(tagsCache.tags()).willReturn(Observable.just(tags))

        presenter.onAttachView(view)

        verify(view).setTags(tags)
    }

    @Test
    fun startsTagEditWhenSelectingATagAndDisplayTypeIsView() {
        val presenter = presenterWithDisplayTypeView()
        val tag = aTag()
        presenter.onAttachView(view)

        selectTag(tag)

        verify(view).startTagEdit(tag)
    }

    @Test
    fun canSelectMultipleTagsWhenDisplayTypeIsMultiChoice() {
        val presenter = presenterWithDisplayTypeMultiChoice()
        val tag1 = aTag()
        val tag2 = aTag()
        presenter.onAttachView(view)

        selectTag(tag1)
        selectTag(tag2)

        presenter.onDetachView(view)
        presenter.onAttachView(view)
        verify(view).setSelectedTags(setOf(tag1, tag2))
    }

    @Test
    fun selectingAlreadySelectedItemDeselectsItWhenDisplayTypeIsMultiChoice() {
        val presenter = presenterWithDisplayTypeMultiChoice()
        val tag1 = aTag()
        val tag2 = aTag()
        presenter.onAttachView(view)

        selectTag(tag1)
        selectTag(tag2)
        selectTag(tag2)

        presenter.onDetachView(view)
        presenter.onAttachView(view)
        verify(view).setSelectedTags(setOf(tag1))
    }

    @Test
    fun startsResultWithSelectedTagsWhenSavingAndDisplayTypeIsMultiChoice() {
        val presenter = presenterWithDisplayTypeMultiChoice()
        val tag1 = aTag()
        val tag2 = aTag()
        presenter.onAttachView(view)
        selectTag(tag1)
        selectTag(tag2)

        save()

        verify(view).startResult(setOf(tag1, tag2))
    }

    @Test
    fun startsTagEditOnCreateTag() {
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)

        createTag()

        verify(view).startTagEdit(Tag.noTag)
    }

    @Test
    fun removesAndShowsUndoForArchivedTagWhenArchivingATag() {
        val tag = aTag()
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)

        archive(tag)

        verify(view).removeTag(tag)
        verify(view).showUndoForArchivedTag()
    }

    @Test
    fun showsUndoForArchivedTagAfterReattach() {
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)

        archive(aTag())
        presenter.onDetachView(view)
        presenter.onAttachView(view)

        verify(view, times(2)).showUndoForArchivedTag()
    }

    @Test
    fun archivesATagOnArchiveCommit() {
        throw UnsupportedOperationException()
    }

    @Test
    fun insertsTagBackOnUndoArchive() {
        throw UnsupportedOperationException()
    }

    private fun selectTag(tagToSelect: Tag) = tagSelectedSubject.onNext(tagToSelect)

    private fun save() = saveSubject.onNext(Unit)

    private fun archive(tag: Tag) {
        archiveSubject.onNext(tag)
    }

    private fun createTag() {
        tagCreateSubject.onNext(Unit)
    }

    private fun presenterWithDisplayTypeView() = TagsPresenter(tagsCache, TagsPresenter.DisplayType.VIEW)

    private fun presenterWithDisplayTypeMultiChoice() = TagsPresenter(tagsCache, TagsPresenter.DisplayType.MULTI_CHOICE)
}

