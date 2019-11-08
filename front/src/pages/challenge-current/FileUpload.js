import React from 'react';
import {useDropzone} from 'react-dropzone';
import styles from './FileUpload.module.css'

export default ({ updateFiles }) => {
    const {acceptedFiles, getRootProps, getInputProps} = useDropzone();

    const files = acceptedFiles.map(file => (
        <li key={file.path}>
            {file.path} - {file.size} bytes
        </li>
    ));

    updateFiles(acceptedFiles)
    return (
        <section className={styles.fileupload}>
            <div {...getRootProps({className: styles.dropzone})}>
                <input {...getInputProps()} />
                <p>Кликни для загрузки или перетащи файлы</p>
            </div>
            {files &&
            <aside>
                <h4>Загружено:</h4>
                <ul>{files}</ul>
            </aside>
            }
        </section>
    );
}
